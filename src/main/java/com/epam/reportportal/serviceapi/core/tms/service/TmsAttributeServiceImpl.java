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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRS;
import com.epam.reportportal.serviceapi.core.tms.mapper.TmsAttributeMapper;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Filter;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.TmsAttributeRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.filterable.TmsAttributeFilterableRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsAttribute;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ErrorType;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import com.epam.reportportal.serviceapi.model.Page;
import com.epam.reportportal.serviceapi.ws.converter.PagedResourcesAssembler;
import jakarta.persistence.EntityExistsException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TmsAttributeServiceImpl implements TmsAttributeService {

  private final TmsAttributeFilterableRepository tmsAttributeFilterableRepository;
  private final TmsAttributeRepository tmsAttributeRepository;
  private final TmsAttributeMapper tmsAttributeMapper;

  @Override
  @Transactional
  public TmsAttributeRS create(TmsAttributeRQ request) {
    validateKeyUniqueness(request.getKey());

    var entity = tmsAttributeMapper.convertToTmsAttribute(request);
    var savedEntity = tmsAttributeRepository.save(entity);

    return tmsAttributeMapper.convertToTmsAttributeRS(savedEntity);
  }

  @Override
  @Transactional
  public TmsAttributeRS patch(Long attributeId, TmsAttributeRQ request) {
    var existingAttribute = findAttributeById(attributeId);

    if (Objects.nonNull(request.getKey())
        && !request.getKey().equals(existingAttribute.getKey())) {
      validateKeyUniqueness(request.getKey());
    }

    tmsAttributeMapper.patch(existingAttribute, request);

    return tmsAttributeMapper.convertToTmsAttributeRS(
        tmsAttributeRepository.save(existingAttribute)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TmsAttributeRS> getAll(Filter filter, Pageable pageable) {
    return PagedResourcesAssembler
        .pageConverter(tmsAttributeMapper::convertToTmsAttributeRS)
        .apply(tmsAttributeFilterableRepository.findByFilter(filter, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public TmsAttributeRS getById(Long attributeId) {
    return tmsAttributeMapper.convertToTmsAttributeRS(
        findAttributeById(attributeId)
    );
  }

  private TmsAttribute findAttributeById(Long attributeId) {
    return tmsAttributeRepository
        .findById(attributeId)
        .orElseThrow(() -> new ReportPortalException(ErrorType.NOT_FOUND,
            "TMS Attribute with id '" + attributeId + "' not found"));
  }

  private void validateKeyUniqueness(String key) {
    if (tmsAttributeRepository.existsByKey(key)) {
      throw new EntityExistsException(
          "TMS Attribute with key '" + key + "' already exists");
    }
  }
}
