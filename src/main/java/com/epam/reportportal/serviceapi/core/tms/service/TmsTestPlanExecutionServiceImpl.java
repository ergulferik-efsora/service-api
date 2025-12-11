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

package com.epam.reportportal.serviceapi.core.tms.service;

import com.epam.reportportal.serviceapi.core.tms.mapper.TmsTestPlanExecutionMapper;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsTestPlanStatisticsRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlan;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlanExecutionStatistic;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlanWithStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TmsTestPlanExecutionServiceImpl implements TmsTestPlanExecutionService {

  private final TmsTestPlanStatisticsRepository tmsTestPlanStatisticsRepository;
  private final TmsTestPlanExecutionMapper tmsTestPlanExecutionMapper;

  @Override
  @Transactional(readOnly = true)
  public TmsTestPlanExecutionStatistic getStatisticsForTestPlan(Long testPlanId) {
    log.debug("Loading execution statistics for test plan: {}", testPlanId);

    try {
      var statistics = tmsTestPlanStatisticsRepository
          .getExecutionStatisticsByTestPlanId(testPlanId);

      if (statistics == null) {
        log.warn("No statistics found for test plan {}, returning empty", testPlanId);
        return tmsTestPlanExecutionMapper.createEmptyStatistics();
      }

      log.debug("Loaded statistics for test plan {}: total={}, covered={}",
          testPlanId, statistics.getTotal(), statistics.getCovered());

      return statistics;
    } catch (Exception e) {
      log.error("Failed to load statistics for test plan {}", testPlanId, e);
      return tmsTestPlanExecutionMapper.createEmptyStatistics();
    }
  }

  @Transactional(readOnly = true)
  @Override
  public TmsTestPlanWithStatistic enrichWithStatistics(TmsTestPlan testPlan) {
    if (testPlan == null) {
      log.warn("Attempted to enrich null test plan with statistics");
      return null;
    }

    log.debug("Enriching test plan {} with statistics", testPlan.getId());

    var statistics = getStatisticsForTestPlan(testPlan.getId());

    log.debug("Successfully enriched test plan {} with statistics: total={}, covered={}",
        testPlan.getId(), statistics.getTotal(), statistics.getCovered());

    return TmsTestPlanWithStatistic.of(testPlan, statistics);
  }
}
