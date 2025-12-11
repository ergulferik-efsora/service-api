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
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "TMS Manual Launch response")
public class TmsManualLaunchRS {

  @Schema(description = "Launch ID", example = "1")
  private Long id;

  @Schema(description = "Launch name", example = "123")
  private String name;

  @Schema(description = "Launch start time", example = "2025-12-20T23:10:01")
  @JsonProperty("startTime")
  private Instant startTime;

  @Schema(description = "Launch end time")
  @JsonProperty("endTime")
  private Instant endTime;

  @Schema(description = "Launch created time", example = "2025-12-20T23:10:01")
  @JsonProperty("createdAt")
  private Instant createdAt;

  @Schema(description = "Launch number", example = "1")
  private Integer number;

  @Schema(description = "Launch status", example = "IN_PROGRESS")
  private String status;

  @Schema(description = "Test plan information")
  @JsonProperty("testPlan")
  private TmsManualLaunchTestPlanRS testPlan;

  @Schema(description = "Execution statistics")
  @JsonProperty("executionStatistic")
  private TmsManualLaunchExecutionStatisticRS executionStatistic;

  @Schema(description = "Launch attributes")
  private List<TmsManualLaunchAttributeRS> attributes;
}
