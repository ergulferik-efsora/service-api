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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of current feature flags.
 *
 * @author <a href="mailto:ivan_kustau@epam.com">Ivan Kustau</a>
 */
public enum FeatureFlag {
  SINGLE_BUCKET("singleBucket"),
  DEFAULT_LDAP_ENCODER("defaultLdapEncoder");

  private final String name;

  FeatureFlag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /**
   * Returns {@link Optional} of {@link FeatureFlag} by string.
   *
   * @param name Name of feature flag
   * @return {@link Optional} of {@link FeatureFlag} by string
   */
  public static Optional<FeatureFlag> fromString(String name) {
    return Optional.ofNullable(name).flatMap(
        str -> Arrays.stream(values()).filter(it -> it.name.equalsIgnoreCase(str)).findAny());

  }
}
