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

package com.epam.reportportal.serviceapi.core.tms.service;

import com.epam.reportportal.serviceapi.core.tms.dto.UploadAttachmentRS;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsAttachment;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing TMS attachments.
 */
public interface TmsAttachmentService {

  /**
   * Uploads an attachment file and creates a TmsAttachment entity with TTL.
   *
   * @param file the multipart file to upload
   * @return the created TmsAttachment with TTL set
   */
  UploadAttachmentRS uploadAttachment(MultipartFile file);

  /**
   * Downloads an attachment by ID.
   *
   * @param attachmentId the attachment ID
   * @return Optional containing the attachment if found
   */
  Optional<TmsAttachment> getTmsAttachment(Long attachmentId);

  /**
   * Deletes an attachment by ID.
   *
   * @param attachmentId the attachment ID
   */
  void deleteAttachment(Long attachmentId);

  /**
   * Removes TTL from attachments when they are permanently associated with test cases.
   *
   * @param attachmentIds list of attachment IDs
   */
  void removeTtlFromTmsAttachments(List<Long> attachmentIds);

  /**
   * Finds and deletes expired attachments.
   */
  void cleanupExpiredAttachments();

  /**
   * Validates that attachments exist and returns valid ones.
   *
   * @param attachmentIds list of attachment IDs to validate
   * @return list of valid attachments
   */
  List<TmsAttachment> getTmsAttachmentsByIds(List<Long> attachmentIds);

  TmsAttachment duplicateTmsAttachment(TmsAttachment originalAttachment);

  void setExpirationForUnusedAttachments();

  void saveAll(Collection<TmsAttachment> attachments);
}
