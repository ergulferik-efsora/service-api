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

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to easily wraps logic with try/catch blocks
 *
 * @author <a href="mailto:andrei_varabyeu@epam.com">Andrei Varabyeu</a>
 */
public final class Safe {

  private static final Logger LOGGER = LoggerFactory.getLogger(Safe.class);

  private final Action work;
  private final Consumer<Exception> errorCallback;

  private Safe(Action work, Consumer<Exception> errorCallback) {
    this.work = work;
    this.errorCallback = errorCallback;
  }

  /**
   * Executes action in try/catch block ignoring any errors
   *
   * @param action Action to be performed
   */
  public static void safe(Action action) {
    new Safe(action, null).perform();
  }

  /**
   * Executes action in try/catch block and performs callback in case of any error
   *
   * @param action        Action to be executed
   * @param errorCallback Error callback
   */
  public static void safe(Action action, Consumer<Exception> errorCallback) {
    new Safe(action, errorCallback).perform();
  }

  /**
   * Performs action
   */
  private void perform() {
    try {
      work.perform();
    } catch (Exception e) {
      if (null != errorCallback) {
        errorCallback.accept(e);
      } else {
        LOGGER.error("Exception appears in safe block, but not handled. Ignoring...", e);
      }

    }

  }

  @FunctionalInterface
  public interface Action {

    void perform() throws Exception;
  }
}
