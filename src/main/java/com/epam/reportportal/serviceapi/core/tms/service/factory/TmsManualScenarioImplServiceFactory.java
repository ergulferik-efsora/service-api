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

package com.epam.reportportal.serviceapi.core.tms.service.factory;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioType;
import com.epam.reportportal.serviceapi.core.tms.service.TmsManualScenarioImplService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TmsManualScenarioImplServiceFactory {

  private final Map<TmsManualScenarioType, TmsManualScenarioImplService> tmsManualScenarioImplServices;

  public TmsManualScenarioImplServiceFactory(
      List<TmsManualScenarioImplService> tmsManualScenarioImplServices) {
    this.tmsManualScenarioImplServices = tmsManualScenarioImplServices
        .stream()
        .collect(Collectors.toMap(
            TmsManualScenarioImplService::getTmsManualScenarioType,
            Function.identity()
        ));
  }

  public TmsManualScenarioImplService getTmsManualScenarioService(
      TmsManualScenarioType tmsManualScenarioType) {
    var tmsManualScenarioService = tmsManualScenarioImplServices.get(tmsManualScenarioType);
    if (Objects.isNull(tmsManualScenarioService)) {
      throw new UnsupportedOperationException("Unsupported tmsManualScenarioType.");
    } else {
      return tmsManualScenarioService;
    }
  }

  public TmsManualScenarioImplService getTmsManualScenarioService(
      com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.enums.TmsManualScenarioType type) {
    return getTmsManualScenarioService(TmsManualScenarioType.valueOf(type.name()));
  }

  public Collection<TmsManualScenarioImplService> getTmsManualScenarioImplServices() {
    return tmsManualScenarioImplServices.values();
  }
}
