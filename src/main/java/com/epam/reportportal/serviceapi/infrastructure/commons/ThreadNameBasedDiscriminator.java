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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;

/**
 * Adds possibility to have per thread log files in logback
 *
 * @author Andrei Varabyeu
 */
public class ThreadNameBasedDiscriminator implements Discriminator<ILoggingEvent> {

  private static final String KEY = "threadName";

  private boolean started;

  @Override
  public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
    return Thread.currentThread().getName();
  }

  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public void start() {
    started = true;
  }

  @Override
  public void stop() {
    started = false;
  }

  @Override
  public boolean isStarted() {
    return started;
  }
}
