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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsAttachment;
import com.epam.reportportal.serviceapi.core.tms.dto.UploadAttachmentRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import java.time.Duration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Mapper(config = CommonMapperConfig.class)
public abstract class TmsAttachmentMapper {

  @Value("${rp.tms.attachment.ttl:PT24H}")
  protected Duration ttl;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "fileName", source = "file.originalFilename")
  @Mapping(target = "fileType", source = "file.contentType")
  @Mapping(target = "fileSize", source = "file.size")
  @Mapping(target = "pathToFile", source = "fileId")
  @Mapping(target = "expiresAt", expression = "java(java.time.Instant.now().plus(ttl))")
  public abstract TmsAttachment convertToAttachment(String fileId, MultipartFile file);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "fileSize", source = "fileSize")
  @Mapping(target = "fileType", source = "fileType")
  @Mapping(target = "fileName", source = "fileName")
  public abstract UploadAttachmentRS convertToUploadAttachmentRS(TmsAttachment attachment);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "expiresAt", ignore = true) // Note: TTL is not copied, making duplicated attachments permanent
  @Mapping(target = "fileName", source = "originalAttachment.fileName")
  @Mapping(target = "fileType", source = "originalAttachment.fileType")
  @Mapping(target = "fileSize", source = "originalAttachment.fileSize")
  @Mapping(target = "pathToFile", source = "newFileId")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "steps", ignore = true)
  @Mapping(target = "textManualScenarios", ignore = true)
  @Mapping(target = "manualScenarioPreconditions", ignore = true)
  public abstract TmsAttachment duplicateAttachment(TmsAttachment originalAttachment,
      String newFileId);
}
