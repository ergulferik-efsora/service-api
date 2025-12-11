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

package com.epam.reportportal.serviceapi.core.tms.dto;

import lombok.Getter;

@Getter
public enum TmsTestCaseImportFormat {
  JSON("json"),
  CSV("csv");

  private final String value;

  TmsTestCaseImportFormat(String value) {
    this.value = value;
  }

  public static TmsTestCaseImportFormat fromString(String value) {
    for (TmsTestCaseImportFormat format : values()) {
      if (format.value.equalsIgnoreCase(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Unsupported import format: " + value);
  }

  public static TmsTestCaseImportFormat fromFileName(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      throw new IllegalArgumentException("Invalid file name: " + fileName);
    }
    var extension = fileName.substring(fileName.lastIndexOf(".") + 1);
    return fromString(extension);
  }
}
