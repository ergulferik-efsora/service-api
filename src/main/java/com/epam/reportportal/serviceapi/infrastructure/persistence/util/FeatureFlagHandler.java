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

package com.epam.reportportal.serviceapi.infrastructure.persistence.util;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.enums.FeatureFlag;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Component for checking enabled feature flags.
 *
 * @author <a href="mailto:ivan_kustau@epam.com">Ivan Kustau</a>
 */
@Component
public class FeatureFlagHandler {

  private final Set<FeatureFlag> enabledFeatureFlagsSet = new HashSet<>();

  /**
   * Initialises {@link FeatureFlagHandler} by environment variable with enabled feature flags.
   *
   * @param featureFlags Set of enabled feature flags
   */
  public FeatureFlagHandler(
      @Value("#{'${rp.feature.flags}'.split(',')}") Set<String> featureFlags) {

    if (!CollectionUtils.isEmpty(featureFlags)) {
      featureFlags.stream().map(FeatureFlag::fromString).filter(Optional::isPresent)
          .map(Optional::get).forEach(enabledFeatureFlagsSet::add);
    }
  }

  public boolean isEnabled(FeatureFlag featureFlag) {
    return enabledFeatureFlagsSet.contains(featureFlag);
  }
}
