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

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Junction entity for Many-to-Many relationship between TmsTextManualScenario and TmsAttachment.
 */
@Entity
@Table(name = "tms_text_manual_scenario_attachment")
@Getter
@Setter
public class TmsTextManualScenarioAttachment {

  @EmbeddedId
  private TmsTextManualScenarioAttachmentId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("textManualScenarioId")
  @JoinColumn(name = "text_manual_scenario_id", nullable = false)
  private TmsTextManualScenario textManualScenario;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("attachmentId")
  @JoinColumn(name = "attachment_id", nullable = false)
  private TmsAttachment attachment;

  public TmsTextManualScenarioAttachment() {
    this.id = new TmsTextManualScenarioAttachmentId();
  }

  public TmsTextManualScenarioAttachment(TmsTextManualScenario textManualScenario,
      TmsAttachment attachment) {
    this.id = new TmsTextManualScenarioAttachmentId(textManualScenario.getManualScenarioId(),
        attachment.getId());
    this.textManualScenario = textManualScenario;
    this.attachment = attachment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TmsTextManualScenarioAttachment that = (TmsTextManualScenarioAttachment) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
