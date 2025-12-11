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

package com.epam.reportportal.serviceapi.extension;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.ReportPortalUser;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.ProjectRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.organization.OrganizationRepositoryCustom;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization.Organization;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization.OrganizationRole;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.project.Project;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.project.ProjectRole;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.user.UserRole;
import com.epam.reportportal.serviceapi.infrastructure.rules.commons.validation.BusinessRule;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ErrorType;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public abstract class ProjectManagerCommand<T> extends ProjectMemberCommand<T> {

  protected ProjectManagerCommand(ProjectRepository projectRepository,
      OrganizationRepositoryCustom organizationRepository) {
    super(projectRepository, organizationRepository);
  }

  @Override
  protected void validatePermissions(ReportPortalUser user, Project project) {
    Organization organization = organizationRepository.findById(project.getOrganizationId())
        .orElseThrow(
            () -> new ReportPortalException(ErrorType.NOT_FOUND, project.getOrganizationId()));

    if (user.getUserRole() == UserRole.ADMINISTRATOR) {
      return;
    }

    OrganizationRole orgRole = ofNullable(user.getOrganizationDetails())
        .flatMap(detailsMapping -> ofNullable(detailsMapping.get(organization.getName())))
        .map(ReportPortalUser.OrganizationDetails::getOrgRole)
        .orElseThrow(() -> new ReportPortalException(ErrorType.ACCESS_DENIED));

    if (orgRole.sameOrHigherThan(OrganizationRole.MANAGER)) {
      return;
    }

    var projectRole = user.getOrganizationDetails().entrySet().stream()
        .filter(entry -> entry.getKey().equals(organization.getName()))
        .map(Entry::getValue)
        .flatMap(orgDetails -> orgDetails.getProjectDetails().entrySet().stream())
        .map(Entry::getValue)
        .filter(details -> details.getProjectId().equals(project.getId()))
        .map(ReportPortalUser.OrganizationDetails.ProjectDetails::getProjectRole)
        .findFirst()
        .orElseThrow(() -> new ReportPortalException(ErrorType.ACCESS_DENIED));

    BusinessRule.expect(projectRole, ProjectRole.EDITOR::sameOrLowerThan)
        .verify(ErrorType.ACCESS_DENIED);
  }
}
