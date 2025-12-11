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

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetContentProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetProviderChain;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetQueryProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableGetParams;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@Component
public class HealthCheckTableColumnChain implements
    WidgetProviderChain<HealthCheckTableGetParams, Map<String, List<String>>> {

  private final WidgetQueryProvider<HealthCheckTableGetParams> customColumnQueryProvider;
  private final WidgetContentProvider<HealthCheckTableGetParams, Map<String, List<String>>> healthCheckTableColumnProvider;

  @Autowired
  public HealthCheckTableColumnChain(
      WidgetQueryProvider<HealthCheckTableGetParams> customColumnQueryProvider,
      WidgetContentProvider<HealthCheckTableGetParams, Map<String, List<String>>> healthCheckTableColumnProvider) {
    this.customColumnQueryProvider = customColumnQueryProvider;
    this.healthCheckTableColumnProvider = healthCheckTableColumnProvider;
  }

  @Override
  public Map<String, List<String>> apply(HealthCheckTableGetParams params) {
    if (!params.isIncludeCustomColumn()) {
      return Collections.emptyMap();
    }
    return healthCheckTableColumnProvider.apply(customColumnQueryProvider.apply(params), params);
  }

  @Override
  public int resolvePriority(HealthCheckTableGetParams params) {
    return customColumnQueryProvider.getSupportedSorting()
        .stream()
        .filter(sorting -> Objects.nonNull(params.getSort().getOrderFor(sorting)))
        .findAny()
        .map(it -> 1)
        .orElse(0);
  }
}
