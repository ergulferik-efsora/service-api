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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsDataset;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import com.epam.reportportal.serviceapi.core.tms.mapper.factory.TmsDatasetParserFactory;
import java.util.Collection;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Mapper(config = CommonMapperConfig.class, uses = TmsEnvironmentDatasetMapper.class)
public abstract class TmsDatasetMapper {

  @Autowired
  private TmsDatasetParserFactory tmsDatasetParserFactory;

  @Mapping(target = "project.id", source = "projectId")
  @Mapping(target = "data", ignore = true)
  @Mapping(target = "testCases", ignore = true)
  @Mapping(target = "environmentDatasets", ignore = true)
  public abstract TmsDataset convertFromRQ(Long projectId, TmsDatasetRQ tmsDatasetRQ);

  public abstract TmsDatasetRS convertToRS(TmsDataset tmsDataset);

  public abstract List<TmsDatasetRS> convertToRS(Collection<TmsDataset> tmsDatasets);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
  @Mapping(target = "id", ignore = true)
  public abstract void update(@MappingTarget TmsDataset targetDataset, TmsDataset dataset);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "id", ignore = true)
  public abstract void patch(@MappingTarget TmsDataset existingDataset,
      TmsDataset dataset);

  public List<TmsDatasetRQ> convertToRQ(MultipartFile file) {
    return tmsDatasetParserFactory.getParser(file).parse(file);
  }
}
