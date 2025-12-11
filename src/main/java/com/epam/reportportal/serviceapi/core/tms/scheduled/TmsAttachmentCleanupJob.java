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

package com.epam.reportportal.serviceapi.core.tms.scheduled;

import com.epam.reportportal.serviceapi.core.tms.service.TmsAttachmentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled job for cleaning up expired TMS attachments. * * This job runs periodically to remove
 * TMS attachments that have exceeded their TTL * and are not permanently associated with test
 * cases. It helps maintain storage * efficiency by removing temporary files that users uploaded but
 * never used.
 */
@Service
public class TmsAttachmentCleanupJob implements Job {

  private static final Logger LOGGER = LoggerFactory.getLogger(TmsAttachmentCleanupJob.class);

  @Autowired
  private TmsAttachmentService tmsAttachmentService;

  @Override
  @Transactional
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.info("TMS attachment cleanup job has been started");

    try {
      tmsAttachmentService.cleanupExpiredAttachments();
      tmsAttachmentService.setExpirationForUnusedAttachments();
      LOGGER.info("TMS attachment cleanup job completed successfully");
    } catch (Exception e) {
      LOGGER.error("TMS attachment cleanup job failed: {}", e.getMessage(), e);
      throw new JobExecutionException("TMS attachment cleanup failed", e);
    }
  }
}
