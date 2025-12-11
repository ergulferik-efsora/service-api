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

package com.epam.reportportal.serviceapi.core.tms.mapper.importer;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseImportFormat;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseRQ;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class TmsTestCaseJsonImporter implements TmsTestCaseImporter {

  private final ObjectMapper objectMapper;

  @Override
  public TmsTestCaseImportFormat getSupportedFormat() {
    return TmsTestCaseImportFormat.JSON;
  }

  @Override
  @SneakyThrows
  public List<TmsTestCaseRQ> importFromFile(MultipartFile file) {
    try (var inputStream = file.getInputStream()) {
      return objectMapper.readValue(inputStream, new TypeReference<>() {
      });
    }
  }
}
