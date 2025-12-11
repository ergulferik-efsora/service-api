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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioAttachmentRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsAttachment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between TmsAttachment entity and manual scenario attachment DTOs.
 * Uses MapStruct for automatic mapping generation.
 */
@Mapper(config = CommonMapperConfig.class)
public interface TmsManualScenarioAttachmentMapper {

  /**
   * Converts attachment entity to response DTO.
   *
   * @param attachment the attachment entity
   * @return the attachment response DTO
   */
  @Mapping(target = "id", source = "id", numberFormat = "0")
  TmsManualScenarioAttachmentRS toResponse(TmsAttachment attachment);

  /**
   * Converts list of attachments to response DTOs.
   *
   * @param attachments the list of attachment entities
   * @return the list of attachment response DTOs
   */
  List<TmsManualScenarioAttachmentRS> toResponseList(List<TmsAttachment> attachments);
}
