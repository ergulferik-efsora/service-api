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

package com.epam.reportportal.serviceapi.core.tms.validation;

import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ErrorType;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TestFolderIdValidator {

  public void validate(Long testFolderId, String testFolderName) {
    var hasTestFolderId = Objects.nonNull(testFolderId);
    var hasTestFolderName = StringUtils.isNotBlank(testFolderName);

    if ((hasTestFolderId && hasTestFolderName) || (!hasTestFolderId && !hasTestFolderName)) {
      throw new ReportPortalException(
          ErrorType.BAD_REQUEST_ERROR,
          "Either testFolderId or testFolderName must be provided and not empty"
      );
    }
  }
}
