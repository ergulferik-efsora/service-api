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

package com.epam.reportportal.serviceapi.infrastructure.persistence.commons.querygen.constant.tms;

public final class TmsTestPlanCriteriaConstant {

  public static final String CRITERIA_TMS_TEST_PLAN_ID = "id";
  public static final String CRITERIA_TMS_TEST_PLAN_NAME = "name";
  public static final String CRITERIA_TMS_TEST_PLAN_DESCRIPTION = "description";
  public static final String CRITERIA_TMS_TEST_PLAN_PROJECT_ID = "projectId";
  public static final String CRITERIA_TMS_TEST_PLAN_SEARCH = "search";
  public static final String CRITERIA_TMS_TEST_PLAN_CREATED_AT = "createdAt";
  public static final String CRITERIA_TMS_TEST_PLAN_UPDATED_AT = "updatedAt";

  private TmsTestPlanCriteriaConstant() {
    //static only
  }
}
