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

package com.epam.reportportal.serviceapi.infrastructure.commons.template;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import jakarta.inject.Provider;
import java.util.Locale;

/**
 * Factory bean for {@link TemplateEngine}
 *
 * @author Andrei Varabyeu
 */
public class TemplateEngineProvider implements Provider<TemplateEngine> {

  private final String basePackagePath;

  public TemplateEngineProvider() {
    this("/");
  }

  /**
   * @param basePackagePath Base path for templates in classpath
   */
  public TemplateEngineProvider(String basePackagePath) {
    Preconditions.checkArgument(!isNullOrEmpty(basePackagePath),
        "Base path for templates is missed");
    this.basePackagePath = basePackagePath;
  }

  @Override
  public TemplateEngine get() {
    Version version = new Version(2, 3, 34);
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(version);

    cfg.setClassForTemplateLoading(this.getClass(), basePackagePath);

    cfg.setIncompatibleImprovements(version);
    cfg.setDefaultEncoding(Charsets.UTF_8.toString());
    cfg.setLocale(Locale.US);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    return new FreemarkerTemplateEngine(cfg);

  }
}
