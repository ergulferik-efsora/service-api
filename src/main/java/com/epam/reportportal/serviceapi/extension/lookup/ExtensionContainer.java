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

package com.epam.reportportal.serviceapi.extension.lookup;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
@Deprecated(forRemoval = true)
public class ExtensionContainer<T> {

  private final Map<String, T> extensions;

  public ExtensionContainer() {
    this.extensions = new LinkedHashMap<>();
  }

  public void add(String key, T extension) {
    extensions.put(key, extension);
  }

  public void remove(String key) {
    extensions.remove(key);
  }

  public T get(String key) {
    return extensions.get(key);
  }

}
