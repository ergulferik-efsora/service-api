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

package com.epam.reportportal.serviceapi.core.tms.mapper.parser;

import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetDataRQ;
import com.epam.reportportal.serviceapi.core.tms.dto.TmsDatasetRQ;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CsvTmsDatasetParser implements TmsDatasetParser {

  @Override
  public TmsDatasetFileType getTmsDatasetFileType() {
    return TmsDatasetFileType.CSV;
  }

  @Override
  @SneakyThrows
  public List<TmsDatasetRQ> parse(MultipartFile file) {
    var datasetMap = new LinkedHashMap<String, List<TmsDatasetDataRQ>>();
    try (var br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      var headerLine = br.readLine(); //skip first line in CSV file
      String line;

      while ((line = br.readLine()) != null) {
        var lineValues = line.split(",");

        if (lineValues.length != 3) {
          throw new IllegalArgumentException(
              "Invalid CSV format. Each row must have 3 columns: name, key, value.");
        }

        var datasetName = lineValues[0].trim();
        var datasetDataKey = lineValues[1].trim();
        var datasetDataValue = lineValues[2].trim();

        datasetMap
            .computeIfAbsent(datasetName, emptyList -> new ArrayList<>())
            .add(new TmsDatasetDataRQ(datasetDataKey, datasetDataValue));
      }
    }
    return datasetMap
        .entrySet()
        .stream()
        .map(dataset -> TmsDatasetRQ.builder().name(dataset.getKey()).attributes(dataset.getValue())
            .build())
        .toList();
  }
}
