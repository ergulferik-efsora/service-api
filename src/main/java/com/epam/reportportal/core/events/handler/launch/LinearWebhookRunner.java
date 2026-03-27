/*
 * Copyright 2025 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.reportportal.core.events.handler.launch;

import static com.epam.reportportal.core.statistics.StatisticsHelper.extractStatisticsCount;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.DEFECTS_AUTOMATION_BUG_TOTAL;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.DEFECTS_PRODUCT_BUG_TOTAL;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.DEFECTS_SYSTEM_ISSUE_TOTAL;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.DEFECTS_TO_INVESTIGATE_TOTAL;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXECUTIONS_FAILED;
import static com.epam.reportportal.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXECUTIONS_TOTAL;

import com.epam.reportportal.core.events.domain.LaunchFinishedEvent;
import com.epam.reportportal.core.events.handler.ConfigurableEventHandler;
import com.epam.reportportal.core.integration.util.LinearIntegrationService;
import com.epam.reportportal.core.launch.GetLaunchHandler;
import com.epam.reportportal.core.project.GetProjectHandler;
import com.epam.reportportal.infrastructure.persistence.dao.IntegrationRepository;
import com.epam.reportportal.infrastructure.persistence.dao.IntegrationTypeRepository;
import com.epam.reportportal.infrastructure.persistence.dao.LogRepositoryCustom;
import com.epam.reportportal.infrastructure.persistence.dao.TestItemRepository;
import com.epam.reportportal.infrastructure.persistence.dao.organization.OrganizationRepositoryCustom;
import com.epam.reportportal.infrastructure.persistence.entity.ItemAttribute;
import com.epam.reportportal.infrastructure.persistence.entity.enums.LogLevel;
import com.epam.reportportal.infrastructure.persistence.entity.enums.ProjectAttributeEnum;
import com.epam.reportportal.infrastructure.persistence.entity.enums.StatusEnum;
import com.epam.reportportal.infrastructure.persistence.entity.enums.TestItemTypeEnum;
import com.epam.reportportal.infrastructure.persistence.entity.integration.Integration;
import com.epam.reportportal.infrastructure.persistence.entity.integration.IntegrationType;
import com.epam.reportportal.infrastructure.persistence.entity.item.Parameter;
import com.epam.reportportal.infrastructure.persistence.entity.item.TestItem;
import com.epam.reportportal.infrastructure.persistence.entity.launch.Launch;
import com.epam.reportportal.infrastructure.persistence.entity.log.Log;
import com.epam.reportportal.infrastructure.persistence.entity.project.Project;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Creates Linear issues via GraphQL API when a launch finishes with failures.
 * Creates a parent issue for the launch and sub-issues for each failed test.
 * Configuration is read from integration parameters stored in the database.
 */
@Service
public class LinearWebhookRunner
    implements ConfigurableEventHandler<LaunchFinishedEvent, Map<String, String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LinearWebhookRunner.class);

  private static final String LINEAR_GRAPHQL_URL = "https://api.linear.app/graphql";
  private static final int MAX_ERROR_LOG_LENGTH = 3000;
  private static final int MAX_ERROR_LOGS = 5;
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

  private final GetLaunchHandler getLaunchHandler;
  private final GetProjectHandler getProjectHandler;
  private final OrganizationRepositoryCustom organizationRepository;
  private final RestTemplate restTemplate;
  private final TestItemRepository testItemRepository;
  private final LogRepositoryCustom logRepository;
  private final ObjectMapper objectMapper;
  private final IntegrationRepository integrationRepository;
  private final IntegrationTypeRepository integrationTypeRepository;
  private final LinearIntegrationService linearIntegrationService;

  @Autowired
  public LinearWebhookRunner(GetLaunchHandler getLaunchHandler,
      GetProjectHandler getProjectHandler,
      OrganizationRepositoryCustom organizationRepository,
      RestTemplate restTemplate, TestItemRepository testItemRepository,
      LogRepositoryCustom logRepository,
      ObjectMapper objectMapper,
      IntegrationRepository integrationRepository,
      IntegrationTypeRepository integrationTypeRepository,
      LinearIntegrationService linearIntegrationService) {
    this.getLaunchHandler = getLaunchHandler;
    this.getProjectHandler = getProjectHandler;
    this.organizationRepository = organizationRepository;
    this.restTemplate = restTemplate;
    this.testItemRepository = testItemRepository;
    this.logRepository = logRepository;
    this.objectMapper = objectMapper;
    this.integrationRepository = integrationRepository;
    this.integrationTypeRepository = integrationTypeRepository;
    this.linearIntegrationService = linearIntegrationService;
  }

  @Override
  @Transactional(readOnly = true)
  public void handle(LaunchFinishedEvent event, Map<String, String> projectConfig) {
    boolean isLinearEnabled = BooleanUtils.toBoolean(
        projectConfig.get(ProjectAttributeEnum.NOTIFICATIONS_ENABLED.getAttribute()))
        && BooleanUtils.toBoolean(
        projectConfig.get(ProjectAttributeEnum.NOTIFICATIONS_LINEAR_ENABLED.getAttribute()));

    if (!isLinearEnabled) {
      return;
    }

    Launch launch = getLaunchHandler.get(event.getId());

    Integration integration = findLinearIntegration(launch.getProjectId());
    if (integration == null) {
      LOGGER.warn("Linear integration is enabled but no Linear integration is configured "
          + "for project ID {}.", launch.getProjectId());
      return;
    }

    String apiKey = linearIntegrationService.resolveApiKey(integration);
    String teamId = resolveTeamId(integration);

    if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(teamId)) {
      LOGGER.warn("Linear integration is enabled but API key or team ID is not configured.");
      return;
    }

    int failedCount = extractStatisticsCount(EXECUTIONS_FAILED, launch.getStatistics());
    if (failedCount == 0) {
      return;
    }

    try {
      createLinearIssues(launch, event, apiKey, teamId);
    } catch (Exception e) {
      LOGGER.error("Failed to create Linear issues for launch '{}'", launch.getName(), e);
    }
  }

  private Integration findLinearIntegration(Long projectId) {
    Optional<IntegrationType> linearType = integrationTypeRepository.findByName("linear");
    if (linearType.isEmpty()) {
      return null;
    }
    // Try project-level integration first, then fall back to global
    List<Integration> projectIntegrations = integrationRepository
        .findAllByProjectIdAndTypeOrderByCreationDateDesc(projectId, linearType.get());
    if (!projectIntegrations.isEmpty()) {
      return projectIntegrations.get(0);
    }
    List<Integration> globalIntegrations = integrationRepository
        .findAllGlobalByType(linearType.get());
    return globalIntegrations.isEmpty() ? null : globalIntegrations.get(0);
  }

  private String resolveTeamId(Integration integration) {
    if (integration.getParams() != null && integration.getParams().getParams() != null) {
      Object project = integration.getParams().getParams().get("project");
      if (project != null) {
        return String.valueOf(project);
      }
    }
    return null;
  }

  private void createLinearIssues(Launch launch, LaunchFinishedEvent event,
      String apiKey, String teamId) {
    Project project = getProjectHandler.get(launch.getProjectId());

    String orgSlug = organizationRepository.findById(project.getOrganizationId())
        .map(org -> org.getSlug())
        .orElse(null);

    String baseProjectUrl = StringUtils.isNotEmpty(event.getBaseUrl()) && orgSlug != null
        ? String.format("%s/ui/#organizations/%s/projects/%s/launches/all",
        event.getBaseUrl(), orgSlug, project.getSlug())
        : null;

    // Create parent issue for the launch
    String parentIssueId = createParentIssue(launch, baseProjectUrl, apiKey, teamId);
    if (parentIssueId == null) {
      LOGGER.error("Failed to create parent Linear issue for launch '{}'", launch.getName());
      return;
    }

    // Load all items and build lookup map
    List<TestItem> allItems = testItemRepository.findTestItemsByLaunchId(launch.getId());
    Map<Long, TestItem> itemMap = allItems.stream()
        .collect(Collectors.toMap(TestItem::getItemId, item -> item));

    // Find failed leaf items and resolve their test-level parent.
    // Test-level = the item whose parent is a SUITE (or has no parent).
    // This avoids creating issues for assertion steps like "Expect toBe".
    Map<Long, TestItem> failedTests = new LinkedHashMap<>();
    for (TestItem item : allItems) {
      if (item.isHasChildren()) {
        continue;
      }
      if (item.getItemResults() == null
          || !StatusEnum.FAILED.equals(item.getItemResults().getStatus())) {
        continue;
      }
      TestItem testItem = resolveTestLevelItem(item, itemMap);
      failedTests.putIfAbsent(testItem.getItemId(), testItem);
    }

    LOGGER.info("Creating {} sub-issues for failed tests in launch '{}' #{}",
        failedTests.size(), launch.getName(), launch.getNumber());

    // Create sub-issue for each failed test
    for (TestItem item : failedTests.values()) {
      try {
        createSubIssue(parentIssueId, item, launch, baseProjectUrl, apiKey, teamId);
      } catch (Exception e) {
        LOGGER.error("Failed to create sub-issue for test item '{}'", item.getName(), e);
      }
    }
  }

  private String createParentIssue(Launch launch, String launchLink,
      String apiKey, String teamId) {
    int totalTests = extractStatisticsCount(EXECUTIONS_TOTAL, launch.getStatistics());
    int failedTests = extractStatisticsCount(EXECUTIONS_FAILED, launch.getStatistics());
    int productBugs = extractStatisticsCount(DEFECTS_PRODUCT_BUG_TOTAL, launch.getStatistics());
    int autoBugs = extractStatisticsCount(DEFECTS_AUTOMATION_BUG_TOTAL, launch.getStatistics());
    int systemIssues = extractStatisticsCount(DEFECTS_SYSTEM_ISSUE_TOTAL, launch.getStatistics());
    int toInvestigate = extractStatisticsCount(DEFECTS_TO_INVESTIGATE_TOTAL,
        launch.getStatistics());

    String title = String.format("[ReportPortal] Launch '%s' #%d failed",
        launch.getName(), launch.getNumber());

    String description = String.format(
        "Launch **%s** #%d finished with failures.\n\n"
            + "**Statistics:**\n"
            + "- Total: %d\n"
            + "- Failed: %d\n"
            + "- Product Bugs: %d\n"
            + "- Automation Bugs: %d\n"
            + "- System Issues: %d\n"
            + "- To Investigate: %d\n\n"
            + "**Link:** %s\n\n"
            + "See sub-issues for individual failed test details.",
        launch.getName(), launch.getNumber(),
        totalTests, failedTests, productBugs, autoBugs, systemIssues, toInvestigate,
        launchLink != null ? launchLink : "N/A"
    );

    Map<String, Object> variables = Map.of(
        "teamId", teamId,
        "title", title,
        "description", description
    );
    String response = executeGraphQL(
        "mutation($teamId: String!, $title: String!, $description: String!) { "
            + "issueCreate(input: { teamId: $teamId, title: $title, description: $description }) "
            + "{ success issue { id identifier url } } }",
        variables, apiKey
    );

    String issueId = extractIssueId(response);
    if (issueId != null) {
      String identifier = extractField(response, "identifier");
      LOGGER.info("Parent issue {} created for launch '{}' #{}",
          identifier, launch.getName(), launch.getNumber());
    }
    return issueId;
  }

  private void createSubIssue(String parentIssueId, TestItem item, Launch launch,
      String baseProjectUrl, String apiKey, String teamId) {

    // Extract attributes for enriched issue content
    Set<ItemAttribute> attributes = item.getAttributes();
    String testId = extractAttribute(attributes, "testId");
    String subject = extractAttribute(attributes, "subject");
    String priority = extractAttribute(attributes, "priority");

    // Build issue title using testId if available
    String title;
    if (StringUtils.isNotBlank(testId)) {
      title = String.format("[%s] %s", testId, item.getName());
    } else if (StringUtils.isNotBlank(item.getTestCaseId())) {
      title = String.format("[%s] %s", item.getTestCaseId(), item.getName());
    } else {
      title = String.format("[BUG] %s", item.getName());
    }

    StringBuilder desc = new StringBuilder();

    // --- Test Identification ---
    desc.append("## Test Information\n\n");
    if (StringUtils.isNotBlank(testId)) {
      desc.append(String.format("**Test ID:** `%s`\n", testId));
    }
    if (StringUtils.isNotBlank(item.getTestCaseId())) {
      desc.append(String.format("**Test Case ID:** `%s`\n", item.getTestCaseId()));
    }
    desc.append(String.format("**Test Name:** %s\n", item.getName()));
    desc.append(String.format("**Status:** FAILED\n"));
    desc.append(String.format("**Type:** %s\n", item.getType()));

    if (StringUtils.isNotBlank(subject)) {
      desc.append(String.format("**Subject:** %s\n", subject));
    }
    if (StringUtils.isNotBlank(priority)) {
      desc.append(String.format("**Priority:** %s\n", priority));
    }
    if (StringUtils.isNotBlank(item.getCodeRef())) {
      desc.append(String.format("**Code Ref:** `%s`\n", item.getCodeRef()));
    }
    if (StringUtils.isNotBlank(item.getDescription())) {
      desc.append(String.format("**Description:** %s\n", item.getDescription()));
    }

    // --- Attributes / Tags ---
    if (attributes != null && !attributes.isEmpty()) {
      List<String> otherAttrs = attributes.stream()
          .filter(a -> !isKnownAnnotationType(a))
          .map(a -> {
            if (StringUtils.isNotBlank(a.getKey())) {
              return String.format("`%s: %s`", a.getKey(), a.getValue());
            }
            return String.format("`%s`", a.getValue());
          })
          .collect(Collectors.toList());
      if (!otherAttrs.isEmpty()) {
        desc.append(String.format("**Tags:** %s\n", String.join(", ", otherAttrs)));
      }
    }

    // --- Parameters ---
    Set<Parameter> parameters = item.getParameters();
    if (parameters != null && !parameters.isEmpty()) {
      desc.append("\n## Parameters\n\n");
      for (Parameter param : parameters) {
        desc.append(String.format("- **%s:** `%s`\n",
            StringUtils.isNotBlank(param.getKey()) ? param.getKey() : "param",
            param.getValue()));
      }
    }

    // --- Execution Details ---
    desc.append("\n## Execution Details\n\n");
    desc.append(String.format("**Launch:** %s #%d\n", launch.getName(), launch.getNumber()));

    if (item.getStartTime() != null) {
      desc.append(String.format("**Started:** %s (UTC)\n",
          DATE_FORMATTER.format(item.getStartTime())));
    }
    if (item.getItemResults() != null) {
      if (item.getItemResults().getEndTime() != null) {
        desc.append(String.format("**Ended:** %s (UTC)\n",
            DATE_FORMATTER.format(item.getItemResults().getEndTime())));
      }
      if (item.getItemResults().getDuration() != null) {
        desc.append(String.format("**Duration:** %.2fs\n",
            item.getItemResults().getDuration()));
      }
    }

    // --- Defect Classification ---
    if (item.getItemResults() != null && item.getItemResults().getIssue() != null) {
      desc.append("\n## Defect Classification\n\n");
      var issue = item.getItemResults().getIssue();
      if (issue.getIssueType() != null) {
        desc.append(String.format("**Defect Type:** %s\n",
            issue.getIssueType().getLongName()));
      }
      if (StringUtils.isNotBlank(issue.getIssueDescription())) {
        desc.append(String.format("**Comment:** %s\n", issue.getIssueDescription()));
      }
    }

    // --- Error Logs ---
    appendErrorLogs(desc, item, launch);

    // --- ReportPortal Link ---
    if (baseProjectUrl != null && item.getPath() != null) {
      desc.append("\n## Links\n\n");
      String pathSegments = item.getPath().replace(".", "/");
      String itemLogUrl = String.format("%s/%d/%s", baseProjectUrl, launch.getId(),
          pathSegments);
      desc.append(String.format("**ReportPortal:** %s\n", itemLogUrl));
    }

    Map<String, Object> variables = Map.of(
        "teamId", teamId,
        "title", title,
        "description", desc.toString(),
        "parentId", parentIssueId
    );
    String response = executeGraphQL(
        "mutation($teamId: String!, $title: String!, $description: String!, $parentId: String!) { "
            + "issueCreate(input: { teamId: $teamId, title: $title, description: $description, "
            + "parentId: $parentId }) "
            + "{ success issue { id identifier url } } }",
        variables, apiKey
    );

    String identifier = extractField(response, "identifier");
    if (identifier != null) {
      LOGGER.info("Sub-issue {} created for failed test '{}' (testId={})",
          identifier, item.getName(), testId);
    }
  }

  private void appendErrorLogs(StringBuilder desc, TestItem item, Launch launch) {
    try {
      List<Log> errorLogs = logRepository.findLatestUnderTestItemByLaunchIdAndTestItemIdsAndLogLevelGte(
          launch.getId(), item.getItemId(), LogLevel.ERROR_INT, MAX_ERROR_LOGS);

      if (errorLogs == null || errorLogs.isEmpty()) {
        return;
      }

      desc.append("\n## Error Logs\n\n");
      for (Log log : errorLogs) {
        if (StringUtils.isBlank(log.getLogMessage())) {
          continue;
        }
        String message = log.getLogMessage();
        if (message.length() > MAX_ERROR_LOG_LENGTH) {
          message = message.substring(0, MAX_ERROR_LOG_LENGTH) + "\n... (truncated)";
        }
        String level = LogLevel.toLevel(log.getLogLevel()).orElse("ERROR");
        String timestamp = log.getLogTime() != null
            ? DATE_FORMATTER.format(log.getLogTime()) : "";
        desc.append(String.format("**[%s] %s**\n```\n%s\n```\n\n", level, timestamp, message));
      }
    } catch (Exception e) {
      LOGGER.warn("Failed to fetch error logs for test item '{}'", item.getName(), e);
    }
  }

  private String extractAttribute(Set<ItemAttribute> attributes, String key) {
    if (attributes == null) {
      return null;
    }
    return attributes.stream()
        .filter(a -> key.equalsIgnoreCase(a.getKey()))
        .map(ItemAttribute::getValue)
        .findFirst()
        .orElse(null);
  }

  private boolean isKnownAnnotationType(ItemAttribute attr) {
    if (attr.getKey() == null) {
      return false;
    }
    String key = attr.getKey().toLowerCase();
    return "testid".equals(key) || "subject".equals(key) || "priority".equals(key);
  }

  /**
   * Walks up from a leaf item to find the test-level item.
   * The test-level item is the one whose parent is a SUITE or has no parent.
   * This avoids using assertion-level names like "Expect toBe" as issue titles.
   */
  private TestItem resolveTestLevelItem(TestItem leaf, Map<Long, TestItem> itemMap) {
    TestItem current = leaf;
    while (current.getParentId() != null) {
      TestItem parent = itemMap.get(current.getParentId());
      if (parent == null) {
        break;
      }
      // If the parent is at SUITE level (SUITE or STORY), then current is the test-level item
      if (parent.getType() != null
          && parent.getType().getLevel() == TestItemTypeEnum.Constants.SUITE_LEVEL) {
        return current;
      }
      current = parent;
    }
    // If no SUITE parent found, return the highest non-root item we reached
    return current;
  }

  private String executeGraphQL(String query, Map<String, Object> variables, String apiKey) {
    try {
      ObjectNode body = objectMapper.createObjectNode();
      body.put("query", query);
      body.set("variables", objectMapper.valueToTree(variables));

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", apiKey);

      HttpEntity<String> request = new HttpEntity<>(
          objectMapper.writeValueAsString(body), headers);
      return restTemplate.postForObject(LINEAR_GRAPHQL_URL, request, String.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to execute GraphQL query", e);
    }
  }

  private String extractIssueId(String response) {
    return extractField(response, "id");
  }

  private String extractField(String response, String field) {
    try {
      JsonNode root = objectMapper.readTree(response);
      JsonNode issue = root.path("data").path("issueCreate").path("issue");
      if (!issue.isMissingNode() && issue.has(field)) {
        return issue.get(field).asText();
      }
    } catch (Exception e) {
      LOGGER.error("Failed to parse Linear response: {}", response, e);
    }
    return null;
  }

}
