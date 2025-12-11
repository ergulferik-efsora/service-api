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

package com.epam.reportportal.serviceapi.infrastructure.commons;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TikaContentTypeResolver implements ContentTypeResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(TikaContentTypeResolver.class);

  @Override
  public String detectContentType(byte[] data) {
    return detectContentType(new ByteArrayInputStream(data));
  }

  @Override
  public String detectContentType(InputStream data) {
    return detectContentType(TikaInputStream.get(data));
  }

  @Override
  public String detectContentType(Path data) {
    try {
      return detectContentType(TikaInputStream.get(data));
    } catch (IOException e) {
      LOGGER.error("Cannot read data stream", e);
      return MediaType.OCTET_STREAM.toString();
    }
  }

  private String detectContentType(TikaInputStream is) {
    try {
      AutoDetectParser parser = new AutoDetectParser();
      Detector detector = parser.getDetector();
      MediaType mediaType = detector.detect(is, new Metadata());
      return mediaType.toString();
    } catch (IOException e) {
      LOGGER.error("Cannot read data stream", e);
      return MediaType.OCTET_STREAM.toString();
    }
  }

}
