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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTextManualScenarioRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTextManualScenarioRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenario;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTextManualScenario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CommonMapperConfig.class, uses = TmsManualScenarioAttributeMapper.class)
public interface TmsTextManualScenarioMapper {

  @Mapping(target = "manualScenarioId", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  TmsTextManualScenario createTmsManualScenario(TmsTextManualScenarioRQ manualScenarioRQ);

  @Mapping(target = "manualScenarioId", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy =
      NullValuePropertyMappingStrategy.SET_TO_NULL,
      nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION
  )
  void updateTmsManualScenario(
      @MappingTarget TmsTextManualScenario target,
      TmsTextManualScenarioRQ testCaseManualScenarioRQ
  );

  @Mapping(target = "manualScenarioId", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void patchTmsManualScenario(
      @MappingTarget TmsTextManualScenario target,
      TmsTextManualScenarioRQ testCaseManualScenarioRQ
  );

  @Mapping(target = "id", source = "id")
  @Mapping(target = "executionEstimationTime", source = "executionEstimationTime")
  @Mapping(target = "linkToRequirements", source = "linkToRequirements")
  @Mapping(target = "preconditions", source = "preconditions")
  @Mapping(target = "manualScenarioType", source = "type")
  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "instructions", source = "textScenario.instructions")
  @Mapping(target = "expectedResult", source = "textScenario.expectedResult")
  @Mapping(target = "attachments", source = "textScenario.attachments")
  TmsTextManualScenarioRS convert(TmsManualScenario tmsManualScenario);

  @Mapping(target = "manualScenarioId", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  @Mapping(target = "instructions", source = "originalTextScenario.instructions")
  @Mapping(target = "expectedResult", source = "originalTextScenario.expectedResult")
  @Mapping(target = "manualScenario", source = "newScenario")
  TmsTextManualScenario duplicateTextScenario(TmsManualScenario newScenario,
      TmsTextManualScenario originalTextScenario);
}
