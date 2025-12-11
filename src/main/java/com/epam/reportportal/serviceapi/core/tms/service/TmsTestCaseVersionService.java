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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioRQ;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCase;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCaseVersion;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

public interface TmsTestCaseVersionService {

  TmsTestCaseVersion createDefaultTestCaseVersion(TmsTestCase tmsTestCase, @Valid TmsManualScenarioRQ manualScenarioRQ);

  TmsTestCaseVersion updateDefaultTestCaseVersion(TmsTestCase tmsTestCase, @Valid TmsManualScenarioRQ manualScenarioRQ);

  TmsTestCaseVersion patchDefaultTestCaseVersion(TmsTestCase tmsTestCase, @Valid TmsManualScenarioRQ manualScenarioRQ);

  void deleteAllByTestCaseId(Long testCaseId);

  void deleteAllByTestCaseIds(List<Long> testCaseIds);

  void deleteAllByTestFolderId(Long projectId, Long folderId);

  TmsTestCaseVersion getDefaultVersion(Long testCaseId);

  Map<Long, TmsTestCaseVersion> getDefaultVersions(List<Long> testCaseIds);

  /**
   * Duplicates a default version for a new test case.
   *
   * @param newTestCase The new test case entity.
   * @param originalVersion The original version to duplicate.
   * @return The duplicated version.
   */
  TmsTestCaseVersion duplicateDefaultVersion(TmsTestCase newTestCase, TmsTestCaseVersion originalVersion);
}
