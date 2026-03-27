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

package com.epam.reportportal.core.integration.util;

import static com.epam.reportportal.infrastructure.rules.commons.validation.BusinessRule.expect;

import com.epam.reportportal.core.integration.util.property.BtsProperties;
import com.epam.reportportal.core.plugin.PluginBox;
import com.epam.reportportal.infrastructure.persistence.dao.IntegrationRepository;
import com.epam.reportportal.infrastructure.persistence.entity.integration.Integration;
import com.epam.reportportal.infrastructure.rules.exception.ErrorType;
import com.epam.reportportal.infrastructure.rules.exception.ReportPortalException;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Integration service for Linear issue tracker.
 * Authentication is handled via API key stored in integration parameters.
 */
@Service
public class LinearIntegrationService extends BasicIntegrationServiceImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(LinearIntegrationService.class);
  private static final String LINEAR_GRAPHQL_URL = "https://api.linear.app/graphql";
  static final String API_KEY_PARAM = "apiKey";

  private final RestTemplate restTemplate;
  private final BasicTextEncryptor basicTextEncryptor;

  @Autowired
  public LinearIntegrationService(IntegrationRepository integrationRepository,
      PluginBox pluginBox, RestTemplate restTemplate,
      BasicTextEncryptor basicTextEncryptor) {
    super(integrationRepository, pluginBox);
    this.restTemplate = restTemplate;
    this.basicTextEncryptor = basicTextEncryptor;
  }

  @Override
  public Map<String, Object> retrieveCreateParams(String integrationType,
      Map<String, Object> integrationParams) {
    expect(integrationParams, MapUtils::isNotEmpty).verify(ErrorType.BAD_REQUEST_ERROR,
        "No integration params provided");

    Map<String, Object> resultParams = Maps.newHashMapWithExpectedSize(integrationParams.size());

    resultParams.put(BtsProperties.URL.getName(),
        BtsProperties.URL.getParam(integrationParams)
            .orElseThrow(() -> new ReportPortalException(
                ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
                "Linear URL is not specified."))
    );

    resultParams.put(BtsProperties.PROJECT.getName(),
        BtsProperties.PROJECT.getParam(integrationParams)
            .orElseThrow(() -> new ReportPortalException(
                ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
                "Linear team key is not specified."))
    );

    String apiKey = Optional.ofNullable(integrationParams.get(API_KEY_PARAM))
        .map(String::valueOf)
        .filter(StringUtils::isNotBlank)
        .orElseThrow(() -> new ReportPortalException(
            ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            "Linear API key is not specified."));
    resultParams.put(API_KEY_PARAM, basicTextEncryptor.encrypt(apiKey));

    return resultParams;
  }

  @Override
  public Map<String, Object> retrieveUpdatedParams(String integrationType,
      Map<String, Object> integrationParams) {
    Map<String, Object> resultParams = Maps.newHashMapWithExpectedSize(integrationParams.size());

    BtsProperties.URL.getParam(integrationParams)
        .ifPresent(url -> resultParams.put(BtsProperties.URL.getName(), url));

    BtsProperties.PROJECT.getParam(integrationParams)
        .ifPresent(project -> resultParams.put(BtsProperties.PROJECT.getName(), project));

    Optional.ofNullable(integrationParams.get(API_KEY_PARAM))
        .map(String::valueOf)
        .filter(StringUtils::isNotBlank)
        .ifPresent(apiKey -> resultParams.put(API_KEY_PARAM, basicTextEncryptor.encrypt(apiKey)));

    Optional.ofNullable(integrationParams.get("defectFormFields"))
        .ifPresent(defectFormFields -> resultParams.put("defectFormFields", defectFormFields));

    return resultParams;
  }

  @Override
  public boolean checkConnection(Integration integration) {
    String apiKey = resolveApiKey(integration);
    if (StringUtils.isBlank(apiKey)) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          "Linear API key is not configured.");
    }

    try {
      String query = "{\"query\": \"{ viewer { id name } }\"}";

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", apiKey);

      HttpEntity<String> request = new HttpEntity<>(query, headers);
      String response = restTemplate.postForObject(LINEAR_GRAPHQL_URL, request, String.class);

      if (response != null && response.contains("\"id\"")) {
        LOGGER.info("Linear connection test successful.");
        return true;
      }

      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          "Linear connection test failed. Check your API key.");
    } catch (ReportPortalException e) {
      throw e;
    } catch (Exception e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          "Linear connection test failed: " + e.getMessage());
    }
  }

  /**
   * Resolves the API key from integration parameters (decrypting it).
   */
  public String resolveApiKey(Integration integration) {
    if (integration.getParams() != null && integration.getParams().getParams() != null) {
      Object apiKeyParam = integration.getParams().getParams().get(API_KEY_PARAM);
      if (apiKeyParam != null && StringUtils.isNotBlank(String.valueOf(apiKeyParam))) {
        try {
          return basicTextEncryptor.decrypt(String.valueOf(apiKeyParam));
        } catch (Exception e) {
          LOGGER.warn("Failed to decrypt Linear API key, using raw value.");
          return String.valueOf(apiKeyParam);
        }
      }
    }
    return null;
  }
}
