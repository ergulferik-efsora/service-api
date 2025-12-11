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

import java.util.Map;
import org.pf4j.ExtensionPoint;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public interface ReportPortalExtensionPoint extends ExtensionPoint {

  /**
   * Should be provided in the {@link #getPluginParams()} method as a key parameter key. Value is supported commands by
   * plugin
   */
  String ALLOWED_COMMANDS = "allowedCommands";

  /**
   * Should be provided in the {@link #getPluginParams()} method as a key parameter key. Value is supported commands by
   * plugin
   */
  String COMMON_COMMANDS = "commonCommands";

  /**
   * Return available plugin parameters
   *
   * @return Map of plugin params
   */
  Map<String, ?> getPluginParams();

  /**
   * Returns concrete plugin command
   *
   * @param commandName Command name
   * @return {@link CommonPluginCommand}
   */
  CommonPluginCommand getCommonCommand(String commandName);

  /**
   * Returns concrete plugin command for existed integration
   *
   * @param commandName Command name
   * @return {@link PluginCommand}
   */
  PluginCommand getIntegrationCommand(String commandName);

  default IntegrationGroupEnum getIntegrationGroup() {
    return IntegrationGroupEnum.OTHER;
  }

}
