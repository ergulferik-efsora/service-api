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

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsDataset;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsEnvironmentDataset;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsEnvironmentDatasetRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsEnvironmentDatasetRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public abstract class TmsEnvironmentDatasetMapper {

  public Set<TmsEnvironmentDataset> convertToEnvironmentDatasets(
      TmsDataset tmsDataset, Collection<TmsEnvironmentDatasetRQ> environmentDatasetRQs) {
    if (isEmpty(environmentDatasetRQs)) {
      return null;
    }
    return environmentDatasetRQs
        .stream()
        .map(environmentDatasetRQ -> convertToEnvironmentDataset(tmsDataset, environmentDatasetRQ))
        .collect(Collectors.toSet());
  }

  @Mapping(target = "dataset", source = "tmsDataset")
  @Mapping(target = "environment.id", source = "environmentDatasetRQ.environmentId")
  @Mapping(target = "datasetType", source = "environmentDatasetRQ.datasetType")
  @Mapping(target = "id.datasetId", source = "tmsDataset.id")
  @Mapping(target = "id.environmentId", source = "environmentDatasetRQ.environmentId")
  public abstract TmsEnvironmentDataset convertToEnvironmentDataset(TmsDataset tmsDataset,
      TmsEnvironmentDatasetRQ environmentDatasetRQ);

  @Mapping(target = "environmentId", source = "id.environmentId")
  public abstract TmsEnvironmentDatasetRS convertToTmsEnvironmentDatasetRS(
      TmsEnvironmentDataset tmsEnvironmentDataset);
}
