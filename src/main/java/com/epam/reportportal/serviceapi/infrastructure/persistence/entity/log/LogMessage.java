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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.log;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class LogMessage implements Serializable {

  private Long id;
  private Instant logTime;
  private String logMessage;
  private Long itemId;
  private Long launchId;
  private Long projectId;

  public LogMessage(Long id, Instant logTime, String logMessage, Long itemId, Long launchId,
      Long projectId) {
    this.id = id;
    this.logTime = logTime;
    this.logMessage = logMessage;
    this.itemId = itemId;
    this.launchId = launchId;
    this.projectId = projectId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getLogTime() {
    return logTime;
  }

  public void setLogTime(Instant logTime) {
    this.logTime = logTime;
  }

  public String getLogMessage() {
    return logMessage;
  }

  public void setLogMessage(String logMessage) {
    this.logMessage = logMessage;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Long getLaunchId() {
    return launchId;
  }

  public void setLaunchId(Long launchId) {
    this.launchId = launchId;
  }

  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogMessage that = (LogMessage) o;
    return Objects.equals(id, that.id) && Objects.equals(logTime, that.logTime)
        && Objects.equals(logMessage, that.logMessage) && Objects.equals(itemId, that.itemId)
        && Objects.equals(launchId, that.launchId) && Objects.equals(projectId, that.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, logTime, logMessage, itemId, launchId, projectId);
  }
}
