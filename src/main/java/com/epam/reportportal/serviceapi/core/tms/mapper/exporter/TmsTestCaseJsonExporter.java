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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TmsTestCaseJsonExporter implements TmsTestCaseExporter {

  private final ObjectMapper objectMapper;

  @Override
  public TmsTestCaseExportFormat getSupportedFormat() {
    return TmsTestCaseExportFormat.JSON;
  }

  @Override
  @SneakyThrows
  public void export(List<TmsTestCaseRS> testCases, boolean includeAttachments,
      HttpServletResponse response) {
    configureHttpResponse(response, includeAttachments);

    var jsonContent = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(testCases);
    response.getOutputStream().write(jsonContent.getBytes(StandardCharsets.UTF_8));
    response.getOutputStream().flush();
  }

  private void configureHttpResponse(HttpServletResponse response, boolean includeAttachments) {
    response.setContentType("application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    var suffix = includeAttachments ? "_with_attachments" : "";
    var filename = "test_cases_export" + suffix + ".json";
    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
  }
}
