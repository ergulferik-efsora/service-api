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
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsEnvironmentDatasetRepository;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsEnvironmentDatasetRQ;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsEnvironmentDatasetMapper;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsEnvironmentDatasetServiceImpl implements TmsEnvironmentDatasetService {

  private final TmsEnvironmentDatasetRepository tmsEnvironmentDatasetRepository;

  private final TmsEnvironmentDatasetMapper tmsEnvironmentDatasetMapper;

  @Override
  @Transactional
  public void createEnvironmentDataset(TmsDataset tmsDataset,
      Collection<TmsEnvironmentDatasetRQ> environmentDatasetRQs) {
    if (isEmpty(environmentDatasetRQs)) {
      return;
    }
    var tmsEnvironmentDatasets = tmsEnvironmentDatasetMapper.convertToEnvironmentDatasets(
        tmsDataset, environmentDatasetRQs
    );
    tmsDataset.setEnvironmentDatasets(tmsEnvironmentDatasets);
    tmsEnvironmentDatasetRepository.saveAll(tmsEnvironmentDatasets);
  }

  @Override
  @Transactional
  public void upsertEnvironmentDataset(TmsDataset tmsDataset,
      Collection<TmsEnvironmentDatasetRQ> environmentDatasetRQs) {
    tmsEnvironmentDatasetRepository.deleteAllByDataset_Id(tmsDataset.getId());
    createEnvironmentDataset(tmsDataset, environmentDatasetRQs);
  }

  @Override
  @Transactional
  public void addEnvironmentDataset(TmsDataset tmsDataset,
      Collection<TmsEnvironmentDatasetRQ> environmentDatasetRQs) {
    if (isEmpty(environmentDatasetRQs)) {
      return;
    }
    var tmsEnvironmentDatasets = tmsEnvironmentDatasetMapper.convertToEnvironmentDatasets(
        tmsDataset, environmentDatasetRQs
    );
    tmsDataset
        .getEnvironmentDatasets()
        .addAll(tmsEnvironmentDatasets);
    tmsEnvironmentDatasetRepository.saveAll(tmsEnvironmentDatasets);
  }

  @Override
  @Transactional
  public void deleteByDatasetId(Long datasetId) {
    tmsEnvironmentDatasetRepository.deleteAllByDataset_Id(datasetId);
  }
}
