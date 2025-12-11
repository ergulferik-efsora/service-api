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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsStep;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsStepsManualScenario;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsStepRepository;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsStepsManualScenarioRQ;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsStepMapper;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsStepServiceImpl implements TmsStepService {

  private final TmsStepMapper tmsStepMapper;
  private final TmsStepRepository tmsStepRepository;
  private final TmsStepAttachmentService tmsStepAttachmentService;

  @Override
  @Transactional
  public void createSteps(TmsStepsManualScenario tmsManualScenario,
      TmsStepsManualScenarioRQ testCaseManualScenarioRQ) {
    if (CollectionUtils.isEmpty(testCaseManualScenarioRQ.getSteps())) {
      return;
    }

    var stepsRQs = testCaseManualScenarioRQ.getSteps();
    if (CollectionUtils.isEmpty(stepsRQs)) {
      return;
    }
    stepsRQs.forEach(stepRQ -> {
      var tmsStep = tmsStepMapper.convertToTmsStep(stepRQ);

      tmsStep.setStepsManualScenario(tmsManualScenario);

      tmsStepRepository.save(tmsStep);

      if (tmsManualScenario.getSteps() == null) {
        tmsManualScenario.setSteps(new HashSet<>());
      }
      tmsManualScenario.getSteps().add(tmsStep);

      tmsStepAttachmentService.createAttachments(tmsStep, stepRQ);

    });
  }

  @Override
  @Transactional
  public void updateSteps(TmsStepsManualScenario tmsManualScenario,
      TmsStepsManualScenarioRQ testCaseManualScenarioRQ) {
    if (CollectionUtils.isNotEmpty(tmsManualScenario.getSteps())) {
      tmsStepAttachmentService.deleteAllBySteps(tmsManualScenario.getSteps());
      tmsStepRepository.deleteAll(tmsManualScenario.getSteps());
      tmsManualScenario.setSteps(new HashSet<>());
    }

    createSteps(tmsManualScenario, testCaseManualScenarioRQ);
  }

  @Override
  @Transactional
  public void patchSteps(TmsStepsManualScenario tmsManualScenario,
      TmsStepsManualScenarioRQ testCaseManualScenarioRQ) {
    if (testCaseManualScenarioRQ == null || CollectionUtils.isEmpty(
        testCaseManualScenarioRQ.getSteps())) {
      return;
    }

    var stepsRQs = testCaseManualScenarioRQ.getSteps();
    if (CollectionUtils.isEmpty(stepsRQs)) {
      return;
    }

    var existingSteps = tmsManualScenario.getSteps();

    if (existingSteps == null) {
      existingSteps = new HashSet<>(stepsRQs.size());
    }

    var newSteps = stepsRQs
        .stream()
        .map(stepRQ -> {
          var tmsStep = tmsStepMapper.convertToTmsStep(stepRQ);

          tmsStepAttachmentService.createAttachments(tmsStep, stepRQ);

          return tmsStep;
        })
        .collect(Collectors.toSet());

    existingSteps.addAll(newSteps);
    newSteps.forEach(step -> step.setStepsManualScenario(tmsManualScenario));
    tmsStepRepository.saveAll(newSteps);
  }

  @Override
  @Transactional
  public void deleteAllByTestCaseId(Long testCaseId) {
    tmsStepAttachmentService.deleteAllByTestCaseId(testCaseId);
    tmsStepRepository.deleteAllByTestCaseId(testCaseId);
  }

  @Override
  @Transactional
  public void deleteAllByTestCaseIds(List<Long> testCaseIds) {
    if (testCaseIds != null && !testCaseIds.isEmpty()) {
      tmsStepAttachmentService.deleteAllByTestCaseIds(testCaseIds);
      tmsStepRepository.deleteAllByTestCaseIds(testCaseIds);
    }
  }

  @Override
  @Transactional
  public void deleteAllByTestFolderId(Long projectId, Long folderId) {
    tmsStepAttachmentService.deleteStepsByTestFolderId(projectId, folderId);
    tmsStepRepository.deleteStepsByTestFolderId(projectId, folderId);
  }

  @Override
  @Transactional
  public void duplicateSteps(Collection<TmsStep> originalSteps,
      TmsStepsManualScenario newStepsScenario) {
    if (CollectionUtils.isEmpty(originalSteps)) {
      return;
    }

    var duplicatedSteps = originalSteps
        .stream()
        .map(originalStep -> {
          var duplicatedStep = tmsStepMapper.duplicateStep(originalStep, newStepsScenario);

          if (CollectionUtils.isNotEmpty(originalStep.getAttachments())) {
            tmsStepAttachmentService.duplicateAttachments(originalStep, duplicatedStep);
          }

          return duplicatedStep;
        })
        .collect(Collectors.toSet());

    newStepsScenario.setSteps(duplicatedSteps);
    tmsStepRepository.saveAll(duplicatedSteps);
  }
}
