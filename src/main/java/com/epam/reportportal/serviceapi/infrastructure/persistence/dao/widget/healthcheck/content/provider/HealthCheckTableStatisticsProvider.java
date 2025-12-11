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

package com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.healthcheck.content.provider;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.dao.util.WidgetContentUtil.COMPONENT_HEALTH_CHECK_TABLE_STATS_FETCHER;

import com.epam.reportportal.serviceapi.infrastructure.persistence.dao.widget.WidgetContentProvider;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableGetParams;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.widget.content.healthcheck.HealthCheckTableStatisticsContent;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@Component
public class HealthCheckTableStatisticsProvider implements
    WidgetContentProvider<HealthCheckTableGetParams, Map<String, HealthCheckTableStatisticsContent>> {

  private final DSLContext dslContext;

  @Autowired
  public HealthCheckTableStatisticsProvider(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  public Map<String, HealthCheckTableStatisticsContent> apply(Select<? extends Record> records,
      HealthCheckTableGetParams params) {
    return COMPONENT_HEALTH_CHECK_TABLE_STATS_FETCHER.apply(dslContext.fetch(records), params);
  }
}
