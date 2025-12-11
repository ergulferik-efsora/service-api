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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsDatasetData;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetDataRQ;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public abstract class TmsDatasetDataMapper {

  public abstract List<TmsDatasetData> convertToTmsDatasetData(
      Collection<TmsDatasetDataRQ> tmsDatasetDataRQs);
}
