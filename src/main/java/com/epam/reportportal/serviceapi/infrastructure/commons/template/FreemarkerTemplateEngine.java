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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Implementation of TemplateEngine based on Freemaker
 *
 * @author <a href="mailto:andrei_varabyeu@epam.com">Andrei Varabyeu</a>
 */
public class FreemarkerTemplateEngine implements TemplateEngine {

  private final Configuration cfg;

  public FreemarkerTemplateEngine(Configuration cfg) {
    this.cfg = cfg;
  }

  @Override
  public String merge(String template, Map<?, ?> data) {
    try (StringWriter writer = new StringWriter()) {
      Template tmpl = cfg.getTemplate(template);
      tmpl.process(data, writer);
      return writer.toString();
    } catch (TemplateException | IOException e) {
      throw new RuntimeException("Unable to process template '" + template + "'", e);
    }
  }
}
