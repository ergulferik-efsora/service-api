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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmsTestPlanTestCaseId implements Serializable {

  @Column(name = "test_plan_id", nullable = false)
  private Long testPlanId;

  @Column(name = "test_case_id", nullable = false)
  private Long testCaseId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TmsTestPlanTestCaseId that = (TmsTestPlanTestCaseId) o;
    return Objects.equals(testPlanId, that.testPlanId) &&
        Objects.equals(testCaseId, that.testCaseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(testPlanId, testCaseId);
  }
}
