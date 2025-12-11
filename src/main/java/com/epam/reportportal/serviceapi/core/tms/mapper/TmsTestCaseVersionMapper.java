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

package com.epam.reportportal.serviceapi.core.tms.mapper;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCase;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms.TmsTestCaseVersion;
import com.epam.reportportal.serviceapi.core.tms.mapper.config.CommonMapperConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CommonMapperConfig.class)
public interface TmsTestCaseVersionMapper {

  default TmsTestCaseVersion createDefaultTestCaseVersion() {
    var testCaseVersion = new TmsTestCaseVersion();
    testCaseVersion.setDefault(true);
    return testCaseVersion;
  }

  default TmsTestCaseVersion duplicateDefaultTestCaseVersion(TmsTestCaseVersion originalVersion,
      TmsTestCase newTestCase) {
    var duplicatedVersion = createDefaultTestCaseVersion();
    duplicatedVersion.setName(originalVersion.getName());
    duplicatedVersion.setTestCase(newTestCase);
    return duplicatedVersion;
  }

  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "testCase", ignore = true)
  @Mapping(target = "default", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy =
      NullValuePropertyMappingStrategy.SET_TO_NULL,
      nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION
  )
  void update(@MappingTarget TmsTestCaseVersion target, TmsTestCaseVersion source);

  @Mapping(target = "manualScenario", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "testCase", ignore = true)
  @Mapping(target = "default", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void patch(@MappingTarget TmsTestCaseVersion target, TmsTestCaseVersion source);
}
