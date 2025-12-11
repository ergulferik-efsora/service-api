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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms;

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.ReportPortalRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsStepsManualScenario;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TmsStepsManualScenarioRepository extends
    ReportPortalRepository<TmsStepsManualScenario, Long> {

  @Modifying
  @Query("DELETE FROM TmsStepsManualScenario sms WHERE sms.manualScenarioId IN ("
      + "SELECT ms.id FROM TmsManualScenario ms WHERE ms.testCaseVersion.id IN ("
      + "SELECT tcv.id FROM TmsTestCaseVersion tcv WHERE tcv.testCase.id = :testCaseId))")
  void deleteAllByTestCaseId(@Param("testCaseId") Long testCaseId);

  /**
   * Delete all steps manual scenarios by test case IDs.
   */
  @Modifying
  @Query("DELETE FROM TmsStepsManualScenario sms WHERE sms.manualScenarioId IN ("
      + "SELECT ms.id FROM TmsManualScenario ms WHERE ms.testCaseVersion.id IN ("
      + "SELECT tcv.id FROM TmsTestCaseVersion tcv WHERE tcv.testCase.id IN (:testCaseIds)))")
  void deleteAllByTestCaseIds(@Param("testCaseIds") List<Long> testCaseIds);

  /**
   * Delete steps manual scenarios by test folder ID using recursive hierarchy.
   */
  @Modifying
  @Query(value = "DELETE FROM tms_steps_manual_scenario sms "
      + "WHERE sms.manual_scenario_id IN ("
      + "  SELECT ms.id FROM tms_manual_scenario ms "
      + "  WHERE ms.test_case_version_id IN ("
      + "    SELECT tcv.id FROM tms_test_case_version tcv "
      + "    WHERE tcv.test_case_id IN ("
      + "      SELECT tc.id FROM tms_test_case tc "
      + "      WHERE tc.test_folder_id IN ("
      + "        WITH RECURSIVE folder_hierarchy AS ("
      + "          SELECT tf.id FROM tms_test_folder tf "
      + "          JOIN project p ON tf.project_id = p.id "
      + "          WHERE tf.id = :folderId AND p.id = :projectId "
      + "          UNION ALL "
      + "          SELECT tf.id FROM tms_test_folder tf "
      + "          JOIN folder_hierarchy fh ON tf.parent_id = fh.id "
      + "        ) "
      + "        SELECT id FROM folder_hierarchy "
      + "      ) "
      + "    ) "
      + "  ) "
      + ") ",
      nativeQuery = true)
  void deleteAllByTestFolderId(@Param("projectId") Long projectId,
      @Param("folderId") Long folderId);
}
