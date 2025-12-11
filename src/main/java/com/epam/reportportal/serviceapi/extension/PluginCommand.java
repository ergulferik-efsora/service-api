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

package com.epam.reportportal.serviceapi.extension;

import com.epam.reportportal.serviceapi.api.model.PluginCommandRQ;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.integration.Integration;
import java.util.Map;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public interface PluginCommand<T> extends NamedPluginCommand {

  /**
   * Executes plugin command for existed integration
   *
   * @param integration Configured ReportPortal integration
   * @param params      Plugin Command parameters
   * @return Result
   */
  default T executeCommand(Integration integration, Map<String, Object> params) {
    return null;
  }


  /**
   * Executes plugin command with provided PluginCommandRQ.
   *
   * @param pluginCommandRq Plugin command request object
   * @return Result
   */
  default T executeCommand(Integration integration, PluginCommandRQ pluginCommandRq) {
    return null;
  }
}
