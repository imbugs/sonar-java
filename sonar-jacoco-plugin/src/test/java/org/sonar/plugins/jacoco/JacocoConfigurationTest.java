/*
 * SonarQube Java
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.jacoco;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import static org.fest.assertions.Assertions.assertThat;

public class JacocoConfigurationTest {

  private Settings settings;
  private JacocoConfiguration jacocoSettings;

  @Before
  public void setUp() {
    settings = new Settings(new PropertyDefinitions().addComponents(JacocoConfiguration.getPropertyDefinitions()));
    jacocoSettings = new JacocoConfiguration(settings);
  }

  @Test
  public void should_be_disabled_if_both_path_are_empty() {
    settings.setProperty(JacocoConfiguration.IT_REPORT_PATH_PROPERTY, "");
    settings.setProperty(JacocoConfiguration.REPORT_PATH_PROPERTY, "");
    assertThat(jacocoSettings.isEnabled()).isFalse();
    settings.setProperty(JacocoConfiguration.IT_REPORT_PATH_PROPERTY, "somePath");
    settings.setProperty(JacocoConfiguration.REPORT_PATH_PROPERTY, "");
    assertThat(jacocoSettings.isEnabled()).isTrue();
    settings.setProperty(JacocoConfiguration.IT_REPORT_PATH_PROPERTY, "");
    settings.setProperty(JacocoConfiguration.REPORT_PATH_PROPERTY, "SomePath");
    assertThat(jacocoSettings.isEnabled()).isTrue();
  }

  @Test
  public void defaults() {
    assertThat(jacocoSettings.getReportPath()).isEqualTo("target/jacoco.exec");

    assertThat(jacocoSettings.getItReportPath()).isEqualTo("target/jacoco-it.exec");
  }

  @Test
  public void shouldReturnItReportPath() {
    settings.setProperty(JacocoConfiguration.IT_REPORT_PATH_PROPERTY, "target/it-jacoco-test.exec");

    assertThat(jacocoSettings.getItReportPath()).isEqualTo("target/it-jacoco-test.exec");
  }

  @Test
  public void shouldSetDestfile() {
    settings.setProperty(JacocoConfiguration.REPORT_PATH_PROPERTY, "jacoco.exec");

    assertThat(jacocoSettings.getReportPath()).isEqualTo("jacoco.exec");
  }

  @Test
  public void shouldQuoteDestfileWithSpace() {
    settings.setProperty(JacocoConfiguration.REPORT_PATH_PROPERTY, "folder spaced/jacoco.exec");

    assertThat(jacocoSettings.getReportPath()).isEqualTo("folder spaced/jacoco.exec");
  }

  @Test
  public void should_set_excludes() {
    settings.setProperty(JacocoConfiguration.EXCLUDES_PROPERTY, "org.sonar.api.*");
    assertThat(jacocoSettings.getExcludes()).isEqualTo("org.sonar.api.*");
  }

}
