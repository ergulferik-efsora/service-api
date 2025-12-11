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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.enums.TmsManualScenarioType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "tms_manual_scenario", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmsManualScenario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "execution_estimation_time")
  private Integer executionEstimationTime;

  @Column(name = "link_to_requirements")
  private String linkToRequirements;

  @OneToOne(mappedBy = "manualScenario")
  private TmsManualScenarioPreconditions preconditions;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private TmsManualScenarioType type;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "test_case_version_id")
  private TmsTestCaseVersion testCaseVersion;

  @OneToMany(mappedBy = "manualScenario")
  @Fetch(FetchMode.SUBSELECT)
  private Set<TmsManualScenarioAttribute> attributes;

  @OneToOne(mappedBy = "manualScenario")
  private TmsTextManualScenario textScenario;

  @OneToOne(mappedBy = "manualScenario")
  private TmsStepsManualScenario stepsScenario;
}
