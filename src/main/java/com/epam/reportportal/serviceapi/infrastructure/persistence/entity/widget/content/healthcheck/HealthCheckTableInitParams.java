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

import jakarta.annotation.Nullable;
import java.util.List;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class HealthCheckTableInitParams {

  private final String viewName;
  private final List<String> attributeKeys;

  @Nullable
  private String customKey;

  private HealthCheckTableInitParams(String viewName, List<String> attributeKeys) {
    this.viewName = viewName;
    this.attributeKeys = attributeKeys;
  }

  private HealthCheckTableInitParams(String viewName, List<String> attributeKeys,
      @Nullable String customKey) {
    this.viewName = viewName;
    this.attributeKeys = attributeKeys;
    this.customKey = customKey;
  }

  public static HealthCheckTableInitParams of(String viewName, List<String> attributeKeys) {
    return new HealthCheckTableInitParams(viewName, attributeKeys);
  }

  public static HealthCheckTableInitParams of(String viewName, List<String> attributeKeys,
      @Nullable String customKey) {
    return new HealthCheckTableInitParams(viewName, attributeKeys, customKey);
  }

  public String getViewName() {
    return viewName;
  }

  public List<String> getAttributeKeys() {
    return attributeKeys;
  }

  @Nullable
  public String getCustomKey() {
    return customKey;
  }

  public void setCustomKey(@Nullable String customKey) {
    this.customKey = customKey;
  }
}
