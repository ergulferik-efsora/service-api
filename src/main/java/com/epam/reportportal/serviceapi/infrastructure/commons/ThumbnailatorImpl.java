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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Thumbnailator implementation using <a href="http://code.google.com/p/thumbnailator/">Thumbnailator</a> API
 *
 * @author Andrei Varabyeu
 */
public class ThumbnailatorImpl implements Thumbnailator {

  // 80
  private int width = 80;

  // 60
  private int height = 60;

  /**
   * With default sizes
   */
  public ThumbnailatorImpl() {

  }

  public ThumbnailatorImpl(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public InputStream createThumbnail(InputStream is) throws IOException {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Thumbnails.of(is).size(width, height).toOutputStream(baos);
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }

  @Override
  public byte[] createThumbnail(byte[] data) throws IOException {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Thumbnails.of(new ByteArrayInputStream(data)).size(width, height)
          .toOutputStream(baos);
      return baos.toByteArray();
    }

  }
}
