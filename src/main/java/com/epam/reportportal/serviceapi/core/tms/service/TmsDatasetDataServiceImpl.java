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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsDataset;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsDatasetDataRepository;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetDataRQ;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsDatasetDataMapper;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsDatasetDataServiceImpl implements TmsDatasetDataService {

  private final TmsDatasetDataMapper tmsDatasetDataMapper;
  private final TmsDatasetDataRepository tmsDatasetDataRepository;

  @Override
  @Transactional
  public void createDatasetData(TmsDataset tmsDataset,
      Collection<TmsDatasetDataRQ> tmsDatasetDataRQs) {
    if (isEmpty(tmsDatasetDataRQs)) {
      return;
    }
    var tmsDatasetData = tmsDatasetDataMapper.convertToTmsDatasetData(
        tmsDatasetDataRQs);
    tmsDataset.setData(tmsDatasetData);
    tmsDatasetData.forEach(
        tmsTestPlanAttribute -> tmsTestPlanAttribute.setDataset(tmsDataset));
    tmsDatasetDataRepository.saveAll(tmsDatasetData);
  }

  @Override
  @Transactional
  public void upsertDatasetData(TmsDataset tmsDataset,
      Collection<TmsDatasetDataRQ> tmsDatasetDataRQs) {
    tmsDatasetDataRepository.deleteAllByDataset_Id(tmsDataset.getId());
    createDatasetData(tmsDataset, tmsDatasetDataRQs);
  }

  @Override
  @Transactional
  public void addDatasetData(TmsDataset tmsDataset,
      Collection<TmsDatasetDataRQ> tmsDatasetDataRQs) {
    if (isEmpty(tmsDatasetDataRQs)) {
      return;
    }
    var tmsDatasetData = tmsDatasetDataMapper.convertToTmsDatasetData(
        tmsDatasetDataRQs);
    tmsDataset.getData().addAll(tmsDatasetData);
    tmsDatasetData.forEach(
        tmsTestPlanAttribute -> tmsTestPlanAttribute.setDataset(tmsDataset));
    tmsDatasetDataRepository.saveAll(tmsDatasetData);
  }

  @Override
  @Transactional
  public void deleteByDatasetId(Long datasetId) {
    tmsDatasetDataRepository.deleteAllByDataset_Id(datasetId);
  }
}
