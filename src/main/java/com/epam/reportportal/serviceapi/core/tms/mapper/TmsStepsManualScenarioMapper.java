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

package com.epam.reportportal.serviceapi.core.tms.mapper;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenario;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsStepsManualScenario;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsStepsManualScenarioRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class, uses = {TmsStepMapper.class,
    TmsManualScenarioAttributeMapper.class})
public interface TmsStepsManualScenarioMapper {

  default TmsStepsManualScenario createTmsStepsManualScenario() {
    return TmsStepsManualScenario.builder().build();
  }

  default TmsStepsManualScenario createTmsStepsManualScenario(TmsManualScenario newScenario) {
    var tmsStepsManualScenario = createTmsStepsManualScenario();
    tmsStepsManualScenario.setManualScenario(newScenario);
    return tmsStepsManualScenario;
  }

  @Mapping(target = "id", source = "id")
  @Mapping(target = "executionEstimationTime", source = "executionEstimationTime")
  @Mapping(target = "linkToRequirements", source = "linkToRequirements")
  @Mapping(target = "preconditions", source = "preconditions")
  @Mapping(target = "manualScenarioType", source = "type")
  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "steps", source = "stepsScenario.steps")
  TmsStepsManualScenarioRS convert(TmsManualScenario tmsManualScenario);
}
