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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.tms.filterable;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.constant.GeneralCriteriaConstant.CRITERIA_PROJECT_ID;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.util.ResultFetchers.TMS_TEST_PLAN_FETCHER;

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Condition;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.ConvertibleCondition;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Filter;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.FilterCondition;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.QueryBuilder;
import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.Queryable;
import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.FilterableRepository;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestPlan;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class TmsTestPlanFilterableRepository implements FilterableRepository<TmsTestPlan> {

  private final DSLContext dsl;

  public TmsTestPlanFilterableRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public List<TmsTestPlan> findByFilter(Queryable filter) {
    Set<String> fields = filter.getFilterConditions()
        .stream()
        .map(ConvertibleCondition::getAllConditions)
        .flatMap(Collection::stream)
        .map(FilterCondition::getSearchCriteria)
        .collect(Collectors.toSet());

    return TMS_TEST_PLAN_FETCHER.apply(
        dsl.fetch(QueryBuilder.newBuilder(filter, fields).wrap().build()));
  }

  @Override
  public Page<TmsTestPlan> findByFilter(Queryable filter, Pageable pageable) {
    Set<String> fields = filter.getFilterConditions()
        .stream()
        .map(ConvertibleCondition::getAllConditions)
        .flatMap(Collection::stream)
        .map(FilterCondition::getSearchCriteria)
        .collect(Collectors.toSet());

    fields.addAll(pageable.getSort().get()
        .map(Sort.Order::getProperty)
        .collect(Collectors.toSet()));

    return PageableExecutionUtils.getPage(
        TMS_TEST_PLAN_FETCHER.apply(
            dsl.fetch(QueryBuilder.newBuilder(filter, fields)
                .with(pageable)
                .wrap()
                .withWrapperSort(pageable.getSort())
                .build())),
        pageable,
        () -> dsl.fetchCount(QueryBuilder.newBuilder(filter, fields).build()));
  }

  public Page<Long> findIdsByFilter(Queryable filter, Pageable pageable) {
    //TODO refactor to the correct implementation with a fetching of ids from DB initially
    var fullResults = findByFilter(filter, pageable);

    List<Long> ids = fullResults.getContent()
        .stream()
        .map(TmsTestPlan::getId)
        .collect(Collectors.toList());

    return new PageImpl<>(ids, pageable, fullResults.getTotalElements());
  }

  public Page<Long> findIdsByProjectIdAndFilter(long projectId, Filter filter, Pageable pageable) {
    filter.withCondition(new FilterCondition(Condition.EQUALS,
        false,
        String.valueOf(projectId),
        CRITERIA_PROJECT_ID
    ));
    return findIdsByFilter(filter, pageable);
  }
}
