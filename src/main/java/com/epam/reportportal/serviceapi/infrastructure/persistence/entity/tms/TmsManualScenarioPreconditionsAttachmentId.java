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

/**
 * Composite primary key for TmsManualScenarioPreconditionsAttachment entity.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmsManualScenarioPreconditionsAttachmentId implements Serializable {

  @Column(name = "preconditions_id")
  private Long preconditionsId;

  @Column(name = "attachment_id")
  private Long attachmentId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TmsManualScenarioPreconditionsAttachmentId that = (TmsManualScenarioPreconditionsAttachmentId) o;
    return Objects.equals(preconditionsId, that.preconditionsId)
        && Objects.equals(attachmentId, that.attachmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preconditionsId, attachmentId);
  }

  @Override
  public String toString() {
    return "TmsManualScenarioPreconditionsAttachmentId{" +
        "preconditionsId=" + preconditionsId +
        ", attachmentId=" + attachmentId +
        '}';
  }
}
