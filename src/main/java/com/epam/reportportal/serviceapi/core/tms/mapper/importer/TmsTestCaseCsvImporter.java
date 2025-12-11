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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class TmsTestCaseCsvImporter implements TmsTestCaseImporter {

  private static final String[] EXPECTED_HEADERS = {"name", "description",
      "priority", "externalId"};

  @Override
  public TmsTestCaseImportFormat getSupportedFormat() {
    return TmsTestCaseImportFormat.CSV;
  }

  @Override
  @SneakyThrows
  public List<TmsTestCaseRQ> importFromFile(MultipartFile file) {
    List<TmsTestCaseRQ> testCases = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        var csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
            .setHeader(EXPECTED_HEADERS)
            .setSkipHeaderRecord(true)
            .build())) {

      for (var csvRecord : csvParser) {
        if (csvRecord.size() < EXPECTED_HEADERS.length) {
          throw new IllegalArgumentException(
              "Invalid CSV format. Expected columns: " + String.join(", ", EXPECTED_HEADERS));
        }

        var testCaseRQ = new TmsTestCaseRQ();
        testCaseRQ.setName(csvRecord.get("name"));
        testCaseRQ.setDescription(csvRecord.get("description"));
        testCaseRQ.setPriority(csvRecord.get("priority"));
        testCaseRQ.setExternalId(csvRecord.get("externalId"));

        testCases.add(testCaseRQ);
      }
    }
    return testCases;
  }
}
