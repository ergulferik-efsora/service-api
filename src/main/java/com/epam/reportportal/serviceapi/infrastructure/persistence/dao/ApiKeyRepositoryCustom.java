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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.user.ApiKey;
import java.time.LocalDate;

/**
 * ApiKey repository custom methods.
 *
 * @author Ivan_Kustau
 */
public interface ApiKeyRepositoryCustom {

  /**
   * Update lastUsedAt for apiKey.
   *
   * @param id         id of the ApiKey to update
   * @param hash       hash of ApiKey to update
   * @param lastUsedAt {@link LocalDate}
   * @return updated version of {@link ApiKey}
   */
  ApiKey updateLastUsedAt(Long id, String hash, LocalDate lastUsedAt);
}
