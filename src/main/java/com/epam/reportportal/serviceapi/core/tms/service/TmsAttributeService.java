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

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Filter;
import com.epam.reportportal.serviceapi.model.Page;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsAttributeRS;
import org.springframework.data.domain.Pageable;

public interface TmsAttributeService {

  TmsAttributeRS create(TmsAttributeRQ request);

  TmsAttributeRS patch(Long attributeId, TmsAttributeRQ request);

  Page<TmsAttributeRS> getAll(Filter filter, Pageable pageable);

  TmsAttributeRS getById(Long attributeId);
}
