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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.healthcheck.content;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXECUTIONS_FAILED;
import static java.util.Optional.ofNullable;

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetContentProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetProviderChain;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetQueryProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableGetParams;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableStatisticsContent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@Component
public class HealthCheckTableStatisticsChain
    implements
    WidgetProviderChain<HealthCheckTableGetParams, Map<String, HealthCheckTableStatisticsContent>> {

  private final WidgetQueryProvider<HealthCheckTableGetParams> statisticsQueryProvider;
  private final WidgetContentProvider<HealthCheckTableGetParams, Map<String, HealthCheckTableStatisticsContent>> healthCheckTableStatisticsProvider;

  @Autowired
  public HealthCheckTableStatisticsChain(
      WidgetQueryProvider<HealthCheckTableGetParams> statisticsQueryProvider,
      WidgetContentProvider<HealthCheckTableGetParams, Map<String, HealthCheckTableStatisticsContent>> healthCheckTableStatisticsProvider) {
    this.statisticsQueryProvider = statisticsQueryProvider;
    this.healthCheckTableStatisticsProvider = healthCheckTableStatisticsProvider;
  }

  @Override
  public Map<String, HealthCheckTableStatisticsContent> apply(HealthCheckTableGetParams params) {
    Map<String, HealthCheckTableStatisticsContent> result = statisticsQueryProvider
        .andThen(query -> healthCheckTableStatisticsProvider.apply(query, params))
        .apply(params);
    return getResult(params, result);
  }

  @Override
  public int resolvePriority(HealthCheckTableGetParams params) {
    return statisticsQueryProvider.getSupportedSorting()
        .stream()
        .filter(sorting -> Objects.nonNull(params.getSort().getOrderFor(sorting)))
        .findAny()
        .map(it -> 1)
        .orElse(0);
  }

  /**
   * If sorting order is {@link Sort.Order#isAscending()} and statistics criteria doesn't exist in the result content
   * items without statistics will be put after items with existing criteria. If there is no statistics criteria in the
   * result content it means statistics counter = 0 so items should be put to the top
   *
   * @param params {@link HealthCheckTableGetParams}
   * @param result {@link Map} with 'attribute value' as key and {@link HealthCheckTableStatisticsContent} as value
   * @return resorted or original content
   */
  private Map<String, HealthCheckTableStatisticsContent> getResult(HealthCheckTableGetParams params,
      Map<String, HealthCheckTableStatisticsContent> result) {
    return ofNullable(params.getSort().getOrderFor(EXECUTIONS_FAILED)).filter(
        Sort.Order::isAscending).map(sorting -> {
      Map<String, HealthCheckTableStatisticsContent> resortedResult = result.entrySet()
          .stream()
          .filter(entry -> Objects.isNull(entry.getValue().getStatistics().get(EXECUTIONS_FAILED)))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, curr) -> curr,
              LinkedHashMap::new));
      if (resortedResult.isEmpty()) {
        return result;
      }
      resortedResult.putAll(result);
      return resortedResult;
    }).orElse(result);
  }
}
