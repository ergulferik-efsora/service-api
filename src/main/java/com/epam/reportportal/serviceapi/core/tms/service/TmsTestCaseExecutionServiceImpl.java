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

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsTestCaseExecutionRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCaseExecution;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsTestCaseExecutionServiceImpl implements TmsTestCaseExecutionService {

  private final TmsTestCaseExecutionRepository tmsTestCaseExecutionRepository;

  @Override
  public Map<Long, TmsTestCaseExecution> getLastTestCasesExecutionsByTestCaseIds(List<Long> testCaseIds) {
    return Optional
        .ofNullable(tmsTestCaseExecutionRepository.findLastExecutionsByTestCaseIds(testCaseIds))
        .orElse(Collections.emptyList())
        .stream()
        .collect(Collectors.toMap(
            TmsTestCaseExecution::getTestCaseId, Function.identity()
        ));
  }

  @Override
  public TmsTestCaseExecution getLastTestCaseExecution(Long testCaseId) {
    return tmsTestCaseExecutionRepository
        .findLastExecutionByTestCaseId(testCaseId)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<Long, TmsTestCaseExecution> findLastExecutionsByTestCaseIdsAndTestPlanId(
      List<Long> testCaseIds, Long testPlanId) {

    if (testCaseIds == null || testCaseIds.isEmpty()) {
      return Map.of();
    }

    return tmsTestCaseExecutionRepository
        .findLastExecutionsByTestCaseIdsAndTestPlanId(testCaseIds, testPlanId)
        .stream()
        .collect(Collectors.toMap(
            TmsTestCaseExecution::getTestCaseId,
            Function.identity(),
            (existing, replacement) -> existing // keep first in case of duplicates
        ));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TmsTestCaseExecution> findByTestCaseIdAndTestPlanId(Long testCaseId,
      Long testPlanId) {

    if (testCaseId == null || testPlanId == null) {
      return List.of();
    }

    return tmsTestCaseExecutionRepository.findByTestCaseIdAndTestPlanId(testCaseId, testPlanId);
  }
}
