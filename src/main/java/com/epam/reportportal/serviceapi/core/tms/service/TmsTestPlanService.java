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

import com.epam.reportportal.serviceapi.core.tms.dto.DuplicateTmsTestPlanRS;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseInTestPlanRS;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestFolderRS;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestPlanRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestPlanRS;
import com.epam.reportportal.serviceapi.core.tms.dto.batch.BatchTestCaseOperationResultRS;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Filter;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import com.epam.reportportal.serviceapi.model.Page;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TmsTestPlanService extends CrudService<TmsTestPlanRQ, TmsTestPlanRS, Long> {

  Page<TmsTestPlanRS> getByCriteria(Long projectId, Filter filter, Pageable pageable);

  BatchTestCaseOperationResultRS addTestCasesToPlan(Long projectId, Long testPlanId, @NotEmpty List<Long> testCaseIds);

  BatchTestCaseOperationResultRS removeTestCasesFromPlan(Long projectId, Long testPlanId, @NotEmpty List<Long> testCaseIds);

  boolean addTestCaseToTestPlan(Long testPlanId, Long testCaseId);

  boolean removeSingleTestCaseFromPlan(Long testPlanId, Long testCaseId);

  DuplicateTmsTestPlanRS duplicate(Long projectId, Long testPlanId,
      TmsTestPlanRQ duplicateTestPlanRQ);

  /**
   * Retrieves test cases added to a test plan with pagination.
   * Returns test cases with last execution only (without full execution history).
   *
   * @param projectId  the project ID
   * @param testPlanId the test plan ID
   * @param pageable   pagination parameters
   * @return page of test cases added to the test plan
   */
  Page<TmsTestCaseInTestPlanRS> getTestCasesAddedToPlan(Long projectId, Long testPlanId,
      Pageable pageable);

  /**
   * Retrieves a single test case in test plan with full execution history.
   *
   * @param projectId  the project ID
   * @param testPlanId the test plan ID
   * @param testCaseId the test case ID
   * @return test case with last execution and all executions
   */
  TmsTestCaseInTestPlanRS getTestCaseInTestPlan(Long projectId, Long testPlanId, Long testCaseId);

  /**
   * Verifies that test plan exists in the project.
   *
   * @param projectId  the project ID
   * @param testPlanId the test plan ID
   * @throws ReportPortalException if test plan not found
   */
  void verifyTestPlanExists(Long projectId, Long testPlanId);

  /**
   * Retrieves test folders where test cases added to a test plan with pagination.
   * Returns folders containing test cases that are part of the specified test plan.
   *
   * @param projectId  the project ID
   * @param testPlanId the test plan ID
   * @param pageable   pagination parameters
   * @return page of test folders from the test plan
   */
  Page<TmsTestFolderRS> getTestFoldersFromPlan(Long projectId, Long testPlanId, Pageable pageable);
}
