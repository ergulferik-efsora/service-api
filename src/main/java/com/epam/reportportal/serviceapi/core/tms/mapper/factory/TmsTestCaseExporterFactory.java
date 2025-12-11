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

package com.epam.reportportal.serviceapi.core.tms.mapper.factory;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseExportFormat;
import com.epam.reportportal.serviceapi.core.tms.mapper.exporter.TmsTestCaseExporter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TmsTestCaseExporterFactory {

  private final Map<TmsTestCaseExportFormat, TmsTestCaseExporter> exporters;

  public TmsTestCaseExporterFactory(List<TmsTestCaseExporter> exporters) {
    this.exporters = exporters
        .stream()
        .collect(
            Collectors.toMap(TmsTestCaseExporter::getSupportedFormat, Function.identity())
        );
  }

  public TmsTestCaseExporter getExporter(String format) {
    var exportFormat = TmsTestCaseExportFormat.fromString(format);
    var exporter = exporters.get(exportFormat);

    if (exporter == null) {
      throw new UnsupportedOperationException("Unsupported export format: " + format);
    }

    return exporter;
  }
}
