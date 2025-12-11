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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestCaseImportFormat;
import com.epam.reportportal.serviceapi.core.tms.mapper.importer.TmsTestCaseImporter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TmsTestCaseImporterFactory {

  private final Map<TmsTestCaseImportFormat, TmsTestCaseImporter> importers;

  public TmsTestCaseImporterFactory(List<TmsTestCaseImporter> importers) {
    this.importers = importers
        .stream()
        .collect(
            Collectors.toMap(TmsTestCaseImporter::getSupportedFormat, Function.identity())
        );
  }

  public TmsTestCaseImporter getImporter(MultipartFile file) {
    var format = TmsTestCaseImportFormat.fromFileName(file.getOriginalFilename());
    var importer = importers.get(format);

    if (importer == null) {
      throw new UnsupportedOperationException("Unsupported import format: " + format);
    }

    return importer;
  }
}
