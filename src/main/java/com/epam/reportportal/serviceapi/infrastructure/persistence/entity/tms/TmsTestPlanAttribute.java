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
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tms_test_plan_attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmsTestPlanAttribute implements Serializable {

  @EmbeddedId
  private TmsTestPlanAttributeId id;

  @ManyToOne
  @MapsId(value = "testPlanId")
  @JoinColumn(name = "test_plan_id")
  private TmsTestPlan testPlan;

  @ManyToOne
  @MapsId(value = "attributeId")
  @JoinColumn(name = "attribute_id")
  private TmsAttribute attribute;

  @Column(name = "value")
  private String value;
}
