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

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class LevelEntry {

  private final String key;
  private final String value;

  private LevelEntry(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public static LevelEntry of(String key, String value) {
    return new LevelEntry(key, value);
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
