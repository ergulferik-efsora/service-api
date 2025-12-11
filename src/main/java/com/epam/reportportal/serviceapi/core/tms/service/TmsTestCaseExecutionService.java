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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCaseExecution;
import java.util.List;
import java.util.Map;

public interface TmsTestCaseExecutionService {

  Map<Long, TmsTestCaseExecution> getLastTestCasesExecutionsByTestCaseIds(List<Long> testCaseIds);

  TmsTestCaseExecution getLastTestCaseExecution(Long testCaseId);

  /**
   * Finds last executions for multiple test cases within a specific test plan.
   * Returns a map where key is test case ID and value is the last execution.
   *
   * @param testCaseIds list of test case IDs
   * @param testPlanId  the test plan ID
   * @return map of test case ID to last execution
   */
  Map<Long, TmsTestCaseExecution> findLastExecutionsByTestCaseIdsAndTestPlanId(
      List<Long> testCaseIds, Long testPlanId);

  /**
   * Finds all executions for a specific test case within a test plan.
   * Results are ordered by test_item.start_time DESC (latest first).
   *
   * @param testCaseId the test case ID
   * @param testPlanId the test plan ID
   * @return list of executions ordered by start time descending
   */
  List<TmsTestCaseExecution> findByTestCaseIdAndTestPlanId(Long testCaseId, Long testPlanId);
}
