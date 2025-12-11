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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.project;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.util.ResultFetchers.ORGANIZATION_PROJECT_LIST_FETCHER;

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.QueryBuilder;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Queryable;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.project.ProjectProfile;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationProjectRepositoryImpl implements OrganizationProjectRepository {

  private DSLContext dsl;

  @Autowired
  public void setDsl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public Page<ProjectProfile> getProjectProfileListByFilter(Queryable filter, Pageable pageable) {
    return PageableExecutionUtils.getPage(ORGANIZATION_PROJECT_LIST_FETCHER.apply(
            dsl.fetch(QueryBuilder.newBuilder(filter).with(pageable).build())),
        pageable,
        () -> dsl.fetchCount(QueryBuilder.newBuilder(filter).build()));
  }

}
