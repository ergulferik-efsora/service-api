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

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlan;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsMilestoneRepository;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsMilestoneMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsMilestoneServiceImpl implements TmsMilestoneService {

  private final TmsMilestoneMapper tmsMilestoneMapper;
  private final TmsMilestoneRepository tmsMilestoneRepository;

  @Override
  @Transactional
  public void createTestPlanMilestones(TmsTestPlan tmsTestPlan, List<Long> milestoneIds) {
    if (isEmpty(milestoneIds)) {
      return;
    }
    var milestones = tmsMilestoneMapper.convertToTmsMilestones(milestoneIds);
    milestones.forEach(milestone -> {
      milestone.setTestPlan(tmsTestPlan);
      tmsMilestoneRepository.attachTestPlanToMilestone(tmsTestPlan, milestone.getId());
    });
    tmsTestPlan.setMilestones(milestones);
  }

  @Override
  @Transactional
  public void patchTestPlanMilestones(TmsTestPlan tmsTestPlan, List<Long> milestoneIds) {
    if (isEmpty(milestoneIds)) {
      return;
    }
    var milestones = tmsMilestoneMapper.convertToTmsMilestones(milestoneIds);
    milestones.forEach(milestone -> {
      milestone.setTestPlan(tmsTestPlan);
      tmsMilestoneRepository.attachTestPlanToMilestone(tmsTestPlan, milestone.getId());
    });
    tmsTestPlan.getMilestones().addAll(milestones);
  }

  @Override
  @Transactional
  public void updateTestPlanMilestones(TmsTestPlan tmsTestPlan, List<Long> milestoneIds) {
    if (isEmpty(milestoneIds)) {
      return;
    }
    tmsMilestoneRepository.detachTestPlanFromMilestones(tmsTestPlan.getId());
    var milestones = tmsMilestoneMapper.convertToTmsMilestones(milestoneIds);
    milestones.forEach(milestone -> {
      milestone.setTestPlan(tmsTestPlan);
      tmsMilestoneRepository.attachTestPlanToMilestone(tmsTestPlan, milestone.getId());
    });
    tmsTestPlan.setMilestones(milestones);
  }

  @Override
  @Transactional
  public void detachTestPlanFromMilestones(Long testPlanId) {
    tmsMilestoneRepository.detachTestPlanFromMilestones(testPlanId);
  }

}
