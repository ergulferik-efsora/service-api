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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsManualScenario;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioType;
import java.util.List;

public interface TmsManualScenarioImplService {

  TmsManualScenarioType getTmsManualScenarioType();

  void createTmsManualScenarioImpl(TmsManualScenario tmsManualScenario,
      TmsManualScenarioRQ testCaseManualScenarioRQ);

  void updateTmsManualScenarioImpl(TmsManualScenario manualScenario,
      TmsManualScenarioRQ testCaseManualScenarioRQ);

  void patchTmsManualScenarioImpl(TmsManualScenario manualScenario,
      TmsManualScenarioRQ testCaseManualScenarioRQ);

  void deleteAllByTestCaseId(Long testCaseId);

  void deleteAllByTestCaseIds(List<Long> testCaseIds);

  void deleteAllByTestFolderId(Long projectId, Long folderId);

  /**
   * Duplicates the implementation-specific part of a manual scenario.
   *
   * @param newScenario      The new scenario entity.
   * @param originalScenario The original scenario to duplicate from.
   */
  void duplicateManualScenarioImpl(TmsManualScenario newScenario,
      TmsManualScenario originalScenario);
}
