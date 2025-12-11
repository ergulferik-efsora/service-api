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

import static com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.tables.JClustersTestItem.CLUSTERS_TEST_ITEM;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.cluster.Cluster;
import com.epam.reportportal.serviceapi.infrastructure.persistence.jooq.tables.records.JClustersTestItemRecord;
import java.util.Set;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@Repository
public class ClusterRepositoryCustomImpl implements ClusterRepositoryCustom {

  private final DSLContext dsl;

  @Autowired
  public ClusterRepositoryCustomImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public int saveClusterTestItems(Cluster cluster, Set<Long> itemIds) {
    final InsertValuesStep2<JClustersTestItemRecord, Long, Long> insertQuery = dsl.insertInto(
            CLUSTERS_TEST_ITEM)
        .columns(CLUSTERS_TEST_ITEM.CLUSTER_ID, CLUSTERS_TEST_ITEM.ITEM_ID);

    itemIds.forEach(itemId -> insertQuery.values(cluster.getId(), itemId));

    return insertQuery.execute();
  }
}
