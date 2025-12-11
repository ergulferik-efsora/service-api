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

import com.epam.reportportal.serviceapi.core.tms.mapper.parser.TmsDatasetFileType;
import com.epam.reportportal.serviceapi.core.tms.mapper.parser.TmsDatasetParser;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TmsDatasetParserFactory {

  private Map<TmsDatasetFileType, TmsDatasetParser> tmsDatasetParsers;

  @Autowired
  public void setTmsDatasetParsers(
      List<TmsDatasetParser> tmsDatasetParsers) {
    this.tmsDatasetParsers = tmsDatasetParsers
        .stream()
        .collect(
            Collectors.toMap(TmsDatasetParser::getTmsDatasetFileType, Function.identity())
        );
  }

  public TmsDatasetParser getParser(MultipartFile file) {
    var parser = tmsDatasetParsers.get(getFileType(file));
    if (Objects.isNull(parser)) {
      throw new UnsupportedOperationException("Unsupported file type.");
    } else {
      return parser;
    }
  }

  private TmsDatasetFileType getFileType(MultipartFile file) {
    if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename())) {
      throw new IllegalArgumentException();
    }
    return TmsDatasetFileType.fromString(
        file.getOriginalFilename()
            .substring(file.getOriginalFilename().lastIndexOf(".") + 1)
    );
  }
}
