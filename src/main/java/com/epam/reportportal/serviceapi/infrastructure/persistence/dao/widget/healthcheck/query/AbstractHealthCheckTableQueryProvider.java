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

import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.ITEM_ID;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.KEY;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.constant.WidgetContentRepositoryConstants.VALUE;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.util.JooqFieldNameTransformer.fieldName;

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetQueryProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableGetParams;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.DSL;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public abstract class AbstractHealthCheckTableQueryProvider implements
    WidgetQueryProvider<HealthCheckTableGetParams> {

  private final Set<String> supportedSorting;

  protected AbstractHealthCheckTableQueryProvider(Set<String> supportedSorting) {
    this.supportedSorting = supportedSorting;
  }

  protected abstract Select<? extends Record> contentQuery(HealthCheckTableGetParams params,
      List<Condition> levelConditions);

  @Override
  public Select<? extends Record> apply(HealthCheckTableGetParams params) {

    List<Condition> levelConditions = params.getPreviousLevels()
        .stream()
        .map(levelEntry -> fieldName(params.getViewName(), ITEM_ID).cast(Long.class)
            .in(DSL.selectDistinct(fieldName(ITEM_ID).cast(Long.class))
                .from(params.getViewName())
                .where(fieldName(KEY).cast(String.class)
                    .eq(levelEntry.getKey())
                    .and(fieldName(VALUE).cast(String.class).eq(levelEntry.getValue())))))
        .collect(Collectors.toList());

    return contentQuery(params, levelConditions);
  }

  @Override
  public Set<String> getSupportedSorting() {
    return supportedSorting;
  }
}
