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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsAttribute;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CommonMapperConfig.class)
public interface TmsAttributeMapper {

  @Mapping(target = "id", ignore = true)
  TmsAttribute convertToTmsAttribute(TmsAttributeRQ request);

  TmsAttributeRS convertToTmsAttributeRS(TmsAttribute entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "id", ignore = true)
  void patch(@MappingTarget TmsAttribute entity, TmsAttributeRQ request);
}
