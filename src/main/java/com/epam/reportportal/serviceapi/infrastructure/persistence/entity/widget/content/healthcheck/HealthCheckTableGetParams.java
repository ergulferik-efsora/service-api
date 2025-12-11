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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class HealthCheckTableGetParams {

  private final String viewName;
  private final String currentLevelKey;
  private final Sort sort;

  private final boolean includeCustomColumn;
  private final List<LevelEntry> previousLevels;

  private final Boolean excludeSkippedTests;

  private HealthCheckTableGetParams(String viewName, String currentLevelKey, Sort sort,
      boolean includeCustomColumn, Boolean excludeSkippedTests) {
    this.viewName = viewName;
    this.currentLevelKey = currentLevelKey;
    this.sort = sort;
    this.includeCustomColumn = includeCustomColumn;
    this.excludeSkippedTests = excludeSkippedTests;
    this.previousLevels = Lists.newArrayList();
  }

  private HealthCheckTableGetParams(String viewName, String currentLevelKey, Sort sort,
      boolean includeCustomColumn,
      List<LevelEntry> previousLevels, Boolean excludeSkippedTests) {
    this.viewName = viewName;
    this.currentLevelKey = currentLevelKey;
    this.sort = sort;
    this.includeCustomColumn = includeCustomColumn;
    this.previousLevels = previousLevels;
    this.excludeSkippedTests = excludeSkippedTests;
  }

  public static HealthCheckTableGetParams of(String viewName, String currentLevelKey, Sort sort,
      boolean includeCustomColumn, Boolean excludeSkippedTests) {
    return new HealthCheckTableGetParams(viewName, currentLevelKey, sort, includeCustomColumn,
        excludeSkippedTests);
  }

  public static HealthCheckTableGetParams of(String viewName, String currentLevelKey, Sort sort,
      boolean includeCustomColumn, List<LevelEntry> previousLevels, Boolean excludeSkippedTests) {
    return new HealthCheckTableGetParams(viewName, currentLevelKey, sort, includeCustomColumn,
        previousLevels, excludeSkippedTests);
  }

  public String getViewName() {
    return viewName;
  }

  public String getCurrentLevelKey() {
    return currentLevelKey;
  }

  public Sort getSort() {
    return sort;
  }

  public boolean isIncludeCustomColumn() {
    return includeCustomColumn;
  }

  public List<LevelEntry> getPreviousLevels() {
    return previousLevels;
  }

  public Boolean isExcludeSkippedTests() {
    return excludeSkippedTests;
  }
}
