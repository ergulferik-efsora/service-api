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

package com.epam.reportportal.serviceapi.core.tms.statistics;

import com.epam.reportportal.serviceapi.core.tms.dto.batch.BatchTestCaseOperationError;
import com.epam.reportportal.serviceapi.core.tms.dto.batch.BatchTestCaseOperationResultRS;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Statistics collector for test case duplication operations.
 * Tracks successful test case duplications and errors that occurred during the process.
 */
@Getter
public class TestCaseDuplicationStatistics {

  private final List<Long> successTestCaseIds = new ArrayList<>();
  private final List<BatchTestCaseOperationError> errors = new ArrayList<>();

  /**
   * Records a successful test case duplication.
   *
   * @param testCaseId The ID of the successfully duplicated test case
   */
  public void addSuccess(Long testCaseId) {
    successTestCaseIds.add(testCaseId);
  }

  /**
   * Records a test case duplication error.
   *
   * @param testCaseId   The ID of the test case that failed to duplicate
   * @param errorMessage The error message describing the failure
   */
  public void addError(Long testCaseId, String errorMessage) {
    errors.add(new BatchTestCaseOperationError(testCaseId, errorMessage));
  }

  /**
   * Merges results from batch test case operation into this statistics collector.
   *
   * @param result The batch operation result to merge
   */
  public void merge(BatchTestCaseOperationResultRS result) {
    if (CollectionUtils.isNotEmpty(result.getSuccessTestCaseIds())) {
      successTestCaseIds.addAll(result.getSuccessTestCaseIds());
    }
    if (CollectionUtils.isNotEmpty(result.getErrors())) {
      errors.addAll(result.getErrors());
    }
  }

  /**
   * Gets the total count of test case operations (successful + failed).
   *
   * @return The total count
   */
  public int getTotalCount() {
    return successTestCaseIds.size() + errors.size();
  }

  /**
   * Gets the count of successful test case duplications.
   *
   * @return The success count
   */
  public int getSuccessCount() {
    return successTestCaseIds.size();
  }

  /**
   * Gets the count of failed test case duplications.
   *
   * @return The failure count
   */
  public int getFailureCount() {
    return errors.size();
  }
}
