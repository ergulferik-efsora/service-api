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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseAttributeRQ;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

public interface TmsTestCaseAttributeService {

  void createTestCaseAttributes(@NotNull TmsTestCase tmsTestCase,
      @NotEmpty List<TmsTestCaseAttributeRQ> attributes);

  void updateTestCaseAttributes(@NotNull TmsTestCase tmsTestCase,
      List<TmsTestCaseAttributeRQ> attributes);

  void patchTestCaseAttributes(@NotNull TmsTestCase tmsTestCase,
      List<TmsTestCaseAttributeRQ> attributes);

  void patchTestCaseAttributes(@NotNull @NotEmpty List<TmsTestCase> testCaseIds,
      List<TmsTestCaseAttributeRQ> attributes);

  void deleteAllByTestCaseId(@NotNull Long testCaseId);

  void deleteAllByTestFolderId(@NotNull Long projectId, @NotNull Long testFolderId);

  void deleteAllByTestCaseIds(@NotNull @NotEmpty List<Long> testCaseIds);

  void deleteByTestCaseIdAndAttributeIds(Long testCaseId, List<Long> attributeIds);

  void deleteByTestCaseIdsAndAttributeIds(List<Long> testCaseIds, Collection<Long> attributeIds);

  /**
   * Duplicates test case attributes by linking existing attributes to a new test case.
   *
   * @param originalTestCase The original test case.
   * @param newTestCase      The new test case.
   */
  void duplicateTestCaseAttributes(TmsTestCase originalTestCase, TmsTestCase newTestCase);

  void addAttributesToTestCases(@NotNull @NotEmpty List<Long> testCaseIds,
      @NotNull @NotEmpty Collection<Long> attributeIds);
}
