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
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenarioAttribute;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenarioAttributeId;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioAttributeRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioAttributeRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface TmsManualScenarioAttributeMapper {

  Set<TmsManualScenarioAttribute> convertToTmsManualScenarioAttributes(
      List<TmsManualScenarioAttributeRQ> attributes);

  @Mapping(target = "attribute.id", source = "tmsTestTestAttributeRQ.id")
  @Mapping(target = "id.attributeId", source = "tmsTestTestAttributeRQ.id")
  TmsManualScenarioAttribute convertManualScenarioAttribute(
      TmsManualScenarioAttributeRQ tmsTestTestAttributeRQ);

  @Mapping(target = "id", source = "tmsManualScenarioAttribute.id.attributeId")
  @Mapping(target = "key", source = "tmsManualScenarioAttribute.attribute.key")
  @Mapping(target = "value", source = "tmsManualScenarioAttribute.value")
  TmsManualScenarioAttributeRS convertManualScenarioAttributeRS(
      TmsManualScenarioAttribute tmsManualScenarioAttribute);

  default TmsManualScenarioAttribute duplicateAttribute(
      TmsManualScenarioAttribute originalAttribute,
      TmsManualScenario newScenario) {

    var duplicatedAttribute = new TmsManualScenarioAttribute();

    var newAttributeId = new TmsManualScenarioAttributeId();
    newAttributeId.setManualScenarioId(newScenario.getId());
    newAttributeId.setAttributeId(originalAttribute.getAttribute().getId());

    duplicatedAttribute.setId(newAttributeId);
    duplicatedAttribute.setManualScenario(newScenario);
    duplicatedAttribute.setAttribute(
        originalAttribute.getAttribute()); // using same TmsAttribute
    duplicatedAttribute.setValue(originalAttribute.getValue());

    return duplicatedAttribute;
  }
}
