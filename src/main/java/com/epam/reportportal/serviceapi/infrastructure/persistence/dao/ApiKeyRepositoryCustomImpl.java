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
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

/**
 * ApiKey repository custom method's implementation.
 *
 * @author Ivan_Kustau
 */
public class ApiKeyRepositoryCustomImpl implements ApiKeyRepositoryCustom {

  @Autowired
  private EntityManager entityManager;

  @Transactional
  @Override
  @CachePut(value = "apiKeyCache", key = "#hash")
  public ApiKey updateLastUsedAt(Long id, String hash, LocalDate lastUsedAt) {
    ApiKey apiKey = entityManager.find(ApiKey.class, id);

    if (apiKey != null) {
      apiKey.setLastUsedAt(lastUsedAt);
      entityManager.merge(apiKey);
    }

    return apiKey;
  }
}
