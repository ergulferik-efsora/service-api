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

import com.epam.reportportal.serviceapi.core.tms.dto.batch.BatchPatchTestCaseAttributesRQ;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Validator implementation for {@link ValidBatchPatchTestCaseAttributesRQ} annotation.
 * <p>
 * This validator ensures that at least one field is provided in the BatchPatchTestCaseAttributesRQ object,
 * but not necessarily all.
 * </p>
 */
public class BatchPatchTestCaseAttributesRQValidator
    implements ConstraintValidator<ValidBatchPatchTestCaseAttributesRQ, BatchPatchTestCaseAttributesRQ> {

  @Override
  public void initialize(ValidBatchPatchTestCaseAttributesRQ constraintAnnotation) {
    // No initialization needed
  }

  /**
   * Validates that at least one field of BatchPatchTestCaseAttributesRQ is not null.
   *
   * @param value   the object to validate
   * @param context the validation context
   * @return true if validation passes, false otherwise
   */
  @Override
  public boolean isValid(BatchPatchTestCaseAttributesRQ value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // Let @NotNull handle null objects if needed
    }

    var hasAttributeIdsToAdd = CollectionUtils.isNotEmpty(value.getAttributeIdsToAdd());
    var hasAttributeIdsToRemove = CollectionUtils.isNotEmpty(value.getAttributesToRemove());

    if (!hasAttributeIdsToAdd && !hasAttributeIdsToRemove) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
              "Either attributes to add or to remove must be provided and not empty")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
