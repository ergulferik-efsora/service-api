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
import com.epam.reportportal.serviceapi.core.tms.dto.TmsManualScenarioAttributeRQ;
import java.util.List;

public interface TmsManualScenarioAttributeService {

  void createAttributes(TmsManualScenario tmsManualScenario,
      List<TmsManualScenarioAttributeRQ> attributes);

  void updateAttributes(TmsManualScenario tmsManualScenario,
      List<TmsManualScenarioAttributeRQ> attributes);

  void patchAttributes(TmsManualScenario tmsManualScenario,
      List<TmsManualScenarioAttributeRQ> attributes);

  void deleteAllByTestCaseId(Long testCaseId);

  void deleteAllByTestCaseIds(List<Long> testCaseIds);

  void deleteAllByTestFolderId(Long projectId, Long folderId);

  /**
   * Duplicates attributes from original scenario to new scenario. Uses existing TmsAttribute
   * entities but creates new TmsManualScenarioAttribute associations.
   *
   * @param originalScenario The original scenario with attributes to duplicate.
   * @param newScenario      The new scenario to attach duplicated attributes to.
   */
  void duplicateAttributes(TmsManualScenario originalScenario, TmsManualScenario newScenario);
}
