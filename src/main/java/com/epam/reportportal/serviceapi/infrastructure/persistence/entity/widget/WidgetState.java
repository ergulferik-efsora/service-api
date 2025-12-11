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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public enum WidgetState {

  CREATED("created"),

  RENDERING("rendering"),

  READY("ready"),

  FAILED("failed");

  private final String value;

  WidgetState(String value) {
    this.value = value;
  }

  public static Optional<WidgetState> findByName(@Nullable String name) {
    return Arrays.stream(WidgetState.values())
        .filter(state -> state.getValue().equalsIgnoreCase(name)).findAny();
  }

  public String getValue() {
    return value;
  }
}
