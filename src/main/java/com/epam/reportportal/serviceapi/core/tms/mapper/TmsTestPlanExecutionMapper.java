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

package com.epam.reportportal.serviceapi.core.tms.mapper;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestPlanExecutionStatisticRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlanExecutionStatistic;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface TmsTestPlanExecutionMapper {

  TmsTestPlanExecutionStatisticRS toDto(
      TmsTestPlanExecutionStatistic executionStatistic);

  default TmsTestPlanExecutionStatistic createEmptyStatistics() {
    return TmsTestPlanExecutionStatistic.builder()
        .total(0L)
        .covered(0L)
        .build();
  }
}
