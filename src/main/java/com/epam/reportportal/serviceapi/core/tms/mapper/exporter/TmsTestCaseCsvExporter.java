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

package com.epam.reportportal.serviceapi.core.tms.mapper.exporter;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseExportFormat;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseRS;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TmsTestCaseCsvExporter implements TmsTestCaseExporter {

  private static final String[] CSV_HEADERS = {
      "ID", "Name", "Description", "Test Folder", "Priority"
  };

  @Override
  public TmsTestCaseExportFormat getSupportedFormat() {
    return TmsTestCaseExportFormat.CSV;
  }

  @Override
  @SneakyThrows
  public void export(List<TmsTestCaseRS> testCases, boolean includeAttachments, HttpServletResponse response) {
    configureHttpResponse(response);

    try (var writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        var csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
            .setHeader(CSV_HEADERS)
            .build())) {

      for (var testCase : testCases) {
        csvPrinter.printRecord(
            testCase.getId(),
            testCase.getName(),
            testCase.getDescription(),
            testCase.getTestFolder() != null ? testCase.getTestFolder().getId() : "",
            testCase.getPriority()
        );
      }
      csvPrinter.flush();
    }
  }

  private void configureHttpResponse(HttpServletResponse response) {
    response.setContentType("text/csv");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setHeader("Content-Disposition", "attachment; filename=\"test_cases_export.csv\"");
  }
}
