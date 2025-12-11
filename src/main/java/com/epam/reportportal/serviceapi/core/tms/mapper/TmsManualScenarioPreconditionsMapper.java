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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenarioPreconditions;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioPreconditionsRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioPreconditionsRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between TmsManualScenarioPreconditions entity and its DTOs.
 * Uses MapStruct for automatic mapping generation.
 */
@Mapper(config = CommonMapperConfig.class, uses = {TmsManualScenarioAttachmentMapper.class})
public interface TmsManualScenarioPreconditionsMapper {

  /**
   * Converts request DTO to entity for creation operations.
   *
   * @param request the preconditions request DTO
   * @return the preconditions entity
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  TmsManualScenarioPreconditions toEntity(TmsManualScenarioPreconditionsRQ request);

  /**
   * Converts entity to response DTO.
   *
   * @param entity the preconditions entity
   * @return the preconditions response DTO
   */
  @Mapping(target = "attachments", source = "attachments")
  TmsManualScenarioPreconditionsRS toResponse(TmsManualScenarioPreconditions entity);

  /**
   * Updates existing entity with data from request DTO for full update operations.
   * Sets values to null if they are null in the request (complete replacement).
   *
   * @param target the target entity to update
   * @param request the request DTO with new data
   */
  @BeanMapping(
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
      nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  void update(@MappingTarget TmsManualScenarioPreconditions target,
      TmsManualScenarioPreconditionsRQ request);

  /**
   * Patches existing entity with non-null data from request DTO for partial update operations.
   * Ignores null values in the request (partial update).
   *
   * @param target the target entity to patch
   * @param request the request DTO with new data
   */
  @BeanMapping(
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  void patch(@MappingTarget TmsManualScenarioPreconditions target,
      TmsManualScenarioPreconditionsRQ request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  TmsManualScenarioPreconditions duplicate(TmsManualScenarioPreconditions originalPreconditions);
}
