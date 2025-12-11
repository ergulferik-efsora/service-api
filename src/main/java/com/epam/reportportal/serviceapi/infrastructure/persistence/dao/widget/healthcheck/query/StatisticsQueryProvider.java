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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.healthcheck.query;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXCLUDE_SKIPPED_TABLE;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXECUTIONS_FAILED;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.EXECUTIONS_TOTAL;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.ITEM_ID;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.KEY;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.NAME;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.SUM;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.VALUE;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.util.JooqFieldNameTransformer.fieldName;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.Tables.STATISTICS;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.Tables.STATISTICS_FIELD;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.Tables.TEST_ITEM_RESULTS;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableGetParams;
import com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.enums.JStatusEnum;
import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Select;
import org.jooq.SelectHavingStep;
import org.jooq.SortOrder;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@Component
public class StatisticsQueryProvider extends AbstractHealthCheckTableQueryProvider {

  @Autowired
  private DSLContext dsl;

  public StatisticsQueryProvider() {
    super(Sets.newHashSet(EXECUTIONS_TOTAL, EXECUTIONS_FAILED));
  }

  @Override
  protected Select<? extends Record> contentQuery(HealthCheckTableGetParams params,
      List<Condition> levelConditions) {
    var excludeSkipped = params.isExcludeSkippedTests();

    var resultStatusTable = dsl.select(
            STATISTICS_FIELD.NAME,
            DSL.sum(STATISTICS.S_COUNTER).as(SUM),
            fieldName(VALUE)
        )
        .from(params.getViewName())
        .join(STATISTICS)
        .on(fieldName(params.getViewName(), ITEM_ID)
            .cast(Long.class).eq(STATISTICS.ITEM_ID))
        .join(STATISTICS_FIELD)
        .on(STATISTICS.STATISTICS_FIELD_ID.eq(STATISTICS_FIELD.SF_ID))
        .join(TEST_ITEM_RESULTS)
        .on(TEST_ITEM_RESULTS.RESULT_ID
            .eq(fieldName(params.getViewName(), ITEM_ID).cast(Long.class)))
        .where(fieldName(KEY).cast(String.class)
            .eq(params.getCurrentLevelKey())
            .and(levelConditions.stream().reduce(DSL.noCondition(), Condition::and)))
        .groupBy(fieldName(VALUE), STATISTICS_FIELD.NAME, TEST_ITEM_RESULTS.STATUS)
        .having(filterSkippedTests(excludeSkipped))
        .asTable(EXCLUDE_SKIPPED_TABLE);

    SelectHavingStep<? extends Record3<String, BigDecimal, ?>> selectQuery = DSL.select(
            fieldName(NAME).cast(String.class).as(NAME),
            DSL.sum(fieldName(SUM).cast(Long.class)).as(SUM),
            fieldName(VALUE).as(VALUE))
        .from(resultStatusTable)
        .groupBy(resultStatusTable.field(VALUE), resultStatusTable.field(NAME));

    Optional<Sort.Order> resolvedSort = params.getSort()
        .get()
        .filter(order -> getSupportedSorting().contains(order.getProperty()))
        .findFirst();
    if (resolvedSort.isPresent()) {
      return selectQuery.orderBy(
          DSL.when(fieldName(NAME).cast(String.class).eq(resolvedSort.get().getProperty()),
              fieldName(NAME)),
          resolvedSort.get().isAscending() ?
              fieldName(SUM).sort(SortOrder.ASC) :
              fieldName(SUM).sort(SortOrder.DESC)
      );
    }
    return selectQuery;
  }

  private Condition filterSkippedTests(boolean excludeSkipped) {
    Condition condition = DSL.noCondition();
    if (excludeSkipped) {
      return condition
          .and(TEST_ITEM_RESULTS.STATUS.notEqual(JStatusEnum.SKIPPED));
    }
    return condition;
  }
}
