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

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization.MembershipDetails;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectUserRepositoryCustom {

  Optional<MembershipDetails> findDetailsByUserIdAndProjectKey(Long userId, String projectKey);

  Optional<MembershipDetails> findAdminDetailsProjectKey(String projectKey);

  Page<MembershipDetails> findUserProjectsInOrganization(Long userId, Long organizationId, Pageable pageable);

  Set<Long> findUserProjectIdsInOrganization(Long userId, Long organizationId);
}
