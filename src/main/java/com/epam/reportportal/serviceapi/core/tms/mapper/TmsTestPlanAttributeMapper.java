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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlan;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlanAttribute;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestPlanAttributeRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestPlanAttributeRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public abstract class TmsTestPlanAttributeMapper {

  public abstract Set<TmsTestPlanAttribute> convertToTmsTestPlanAttributes(
      List<TmsTestPlanAttributeRQ> tmsTestPlanAttributeRQList);

  @Mapping(target = "attribute.id", source = "tmsTestPlanAttributeRQ.id")
  @Mapping(target = "id.attributeId", source = "tmsTestPlanAttributeRQ.id")
  public abstract TmsTestPlanAttribute convertToTmsTestPlanAttribute(
      TmsTestPlanAttributeRQ tmsTestPlanAttributeRQ);

  @Mapping(target = "id", source = "tmsTestPlanAttribute.id.attributeId")
  @Mapping(target = "key", source = "tmsTestPlanAttribute.attribute.key")
  @Mapping(target = "value", source = "tmsTestPlanAttribute.value")
  public abstract TmsTestPlanAttributeRS convertTmsPlanAttributeRS(
      TmsTestPlanAttribute tmsTestPlanAttribute);

  @Mapping(target = "id.testPlanId", ignore = true)
  @Mapping(target = "id.attributeId", source = "originalAttribute.id.attributeId")
  @Mapping(target = "attribute", source = "originalAttribute.attribute")
  @Mapping(target = "value", source = "originalAttribute.value")
  @Mapping(target = "testPlan", source = "newTestPlan")
  public abstract TmsTestPlanAttribute duplicateTestPlanAttribute(
      TmsTestPlanAttribute originalAttribute, 
      TmsTestPlan newTestPlan);
}
