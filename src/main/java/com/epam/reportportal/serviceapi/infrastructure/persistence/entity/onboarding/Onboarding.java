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

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.onboarding;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Antonov Maksim
 */
public class Onboarding implements Serializable {

  private Long id;
  private String page;
  private String data;
  private Instant availableFrom;
  private Instant availableTo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Instant getAvailableFrom() {
    return availableFrom;
  }

  public void setAvailableFrom(Instant availableFrom) {
    this.availableFrom = availableFrom;
  }

  public Instant getAvailableTo() {
    return availableTo;
  }

  public void setAvailableTo(Instant availableTo) {
    this.availableTo = availableTo;
  }
}
