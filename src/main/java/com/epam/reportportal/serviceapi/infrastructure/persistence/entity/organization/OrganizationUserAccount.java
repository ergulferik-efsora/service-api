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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.user.UserRole;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.user.UserType;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class OrganizationUserAccount {

  private Long id;
  private String fullName;
  private Instant createdAt;
  private Instant updatedAt;
  private UserRole instanceRole;
  private OrganizationRole orgRole;
  private UserType authProvider;
  private String email;
  private Instant lastLoginAt;
  private String externalId;
  private UUID uuid;
  private Integer projectCount;

}
