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

package com.epam.reportportal.serviceapi.core.tms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation to ensure that either testFolderId or testFolderName is provided.
 * <p>
 * This annotation validates that at least one of the two fields is not null, preventing
 * scenarios where both fields are null which would make the request invalid.
 * </p>
 */
@Documented
@Constraint(validatedBy = TestFolderIdForUpsertTestCaseValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTestFolderIdForUpsertTestCase {

  /**
   * The error message to be returned when validation fails.
   *
   * @return the error message
   */
  String message() default "Either testFolderId or testFolderName must be provided";

  /**
   * Validation groups.
   *
   * @return the validation groups
   */
  Class<?>[] groups() default {};

  /**
   * Payload for extensibility purposes.
   *
   * @return the payload
   */
  Class<? extends Payload>[] payload() default {};
}
