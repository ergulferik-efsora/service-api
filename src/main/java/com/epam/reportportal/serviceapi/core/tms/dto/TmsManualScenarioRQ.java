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

package com.epam.reportportal.serviceapi.core.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base request DTO for manual scenario operations. Contains common information for all
 * types of manual scenarios. This class is extended by concrete implementations for different
 * scenario types.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "manualScenarioType",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TmsStepsManualScenarioRQ.class, name = "STEPS"),
    @JsonSubTypes.Type(value = TmsTextManualScenarioRQ.class, name = "TEXT")
})
@Valid
public abstract class TmsManualScenarioRQ {

  /**
   * Estimated time for execution in minutes. Must be a positive number.
   */
  @Min(value = 1, message = "Execution estimation time must be positive")
  protected Integer executionEstimationTime;

  protected String linkToRequirements;

  /**
   * Type of manual scenario (STEPS or TEXT). Required for proper JSON deserialization.
   */
  @NotNull(message = "Manual scenario type must be specified")
  protected TmsManualScenarioType manualScenarioType;

  /**
   * Preconditions for the test case.
   */
  protected TmsManualScenarioPreconditionsRQ preconditions;

  /**
   * Attributes of the manual scenario.
   */
  @Valid
  protected List<TmsManualScenarioAttributeRQ> attributes;
}
