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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmsTestCaseAttributeId implements Serializable {

  @Column(name = "test_case_id")
  private Long testCaseId;

  @Column(name = "attribute_id")
  private Long attributeId;

  //TODO: override equals and hashCode methods


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TmsTestCaseAttributeId that = (TmsTestCaseAttributeId) o;
    return Objects.equals(testCaseId, that.testCaseId) && Objects.equals(attributeId,
        that.attributeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(testCaseId, attributeId);
  }
}
