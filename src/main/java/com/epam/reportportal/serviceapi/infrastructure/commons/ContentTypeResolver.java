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

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Content type resolver
 *
 * @author Andrei Varabyeu
 */
public interface ContentTypeResolver {

  /**
   * Detects content type of data in input stream
   *
   * @param data Data to be resolved
   * @return detected media type, or <code>application/octet-stream</code>
   */
  String detectContentType(byte[] data);

  /**
   * Detects content type of data in input stream
   *
   * @param data Data to be resolved
   * @return detected media type, or <code>application/octet-stream</code>
   */
  String detectContentType(InputStream data);

  /**
   * Detects content type of data in file
   *
   * @param data Data to be resolved
   * @return detected media type, or <code>application/octet-stream</code>
   */
  String detectContentType(Path data);

}
