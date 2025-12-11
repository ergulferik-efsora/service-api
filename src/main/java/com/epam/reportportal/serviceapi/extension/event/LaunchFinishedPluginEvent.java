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

package com.epam.reportportal.serviceapi.extension.event;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class LaunchFinishedPluginEvent extends LaunchEvent<Long> {

  private final Long projectId;

  private String launchLink;

  public LaunchFinishedPluginEvent(Long source, Long projectId) {
    super(source);
    this.projectId = projectId;
  }

  public LaunchFinishedPluginEvent(Long source, Long projectId, String launchLink) {
    this(source, projectId);
    this.launchLink = launchLink;
  }

  public Long getProjectId() {
    return projectId;
  }

  public String getLaunchLink() {
    return launchLink;
  }
}
