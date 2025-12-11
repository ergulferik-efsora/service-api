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

import static com.epam.reportportal.serviceapi.infrastructure.rules.exception.ErrorType.NOT_FOUND;

import com.epam.reportportal.serviceapi.core.tms.dto.ProductVersionRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsProductVersionRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsProductVersionMapper;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.ProductVersionRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsProductVersion;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductVersionServiceImpl implements ProductVersionService {

  private static final String VERSION_NOT_FOUND_BY_ID = "Product Version with id: %d";

  private final TmsProductVersionMapper productVersionMapper;
  private final ProductVersionRepository productVersionRepository;

  @Override
  @Transactional
  public TmsProductVersionRS create(long projectId, final ProductVersionRQ inputDto) {
    final var productVersion = new TmsProductVersion(null,
        inputDto.version(), inputDto.documentation(), projectId);
    return productVersionMapper.convert(productVersionRepository.save(productVersion));
  }

  @Override
  @Transactional
  public TmsProductVersionRS update(long projectId, final Long productVersionId,
      final ProductVersionRQ inputDto) {
    final var productVersion = new TmsProductVersion(productVersionId,
        inputDto.version(),
        inputDto.documentation(),
        projectId);
    return productVersionMapper.convert(productVersionRepository.save(productVersion));
  }

  @Override
  public TmsProductVersionRS patch(long projectId, Long productVersionId, ProductVersionRQ t) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Transactional
  public void delete(long projectId, Long id) {
    productVersionRepository.deleteByIdAndProjectId(id, projectId);
  }

  @Override
  public TmsProductVersionRS getById(long projectId, Long id) {
    return productVersionRepository.findByProjectIdAndId(projectId, id)
        .map(productVersionMapper::convert)
        .orElseThrow(() -> new ReportPortalException(NOT_FOUND, VERSION_NOT_FOUND_BY_ID.formatted(id)));
  }
}
