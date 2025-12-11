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

import com.epam.reportportal.serviceapi.core.tms.dto.TmsTestFolderExportFileType;
import com.epam.reportportal.serviceapi.core.tms.mapper.exporter.TmsTestFolderExporter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TmsTestFolderExporterFactory {

  private Map<TmsTestFolderExportFileType, TmsTestFolderExporter> tmsTestFolderExporters;

  @Autowired
  public void setTmsTestFolderExporters(
      List<TmsTestFolderExporter> tmsTestFolderExporters) {
    this.tmsTestFolderExporters = tmsTestFolderExporters
        .stream()
        .collect(
            Collectors.toMap(TmsTestFolderExporter::getTmsTestFolderExportFileType,
                Function.identity())
        );
  }

  public TmsTestFolderExporter getExporter(TmsTestFolderExportFileType fileType) {
    var parser = tmsTestFolderExporters.get(fileType);
    if (Objects.isNull(parser)) {
      throw new UnsupportedOperationException("Unsupported file type.");
    } else {
      return parser;
    }
  }
}
