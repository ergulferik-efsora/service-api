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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object that represents a test folder identifier along with the count of test cases
 * associated with that folder in the Test Management System (TMS).
 *
 * <p>This class is primarily used for reporting and analytics purposes when retrieving aggregated
 * data
 * about test folders and their test case counts. It's commonly used in repository queries that need
 * to return both the folder ID and the number of test cases within that folder in a single result.
 * </p>
 *
 * <p>The class serves as a projection object for database queries that aggregate test case counts
 * grouped by test folder, providing efficient data retrieval for TMS dashboard and reporting
 * features.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmsTestFolderIdWithCountOfTestCases {

  private Long testFolderId;
  private Long countOfTestCases;
}
