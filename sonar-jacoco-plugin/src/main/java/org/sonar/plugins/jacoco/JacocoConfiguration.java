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

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

public class JacocoConfiguration implements BatchExtension {

  public static final String REPORT_PATH_PROPERTY = "sonar.jacoco.reportPath";
  public static final String REPORT_PATH_DEFAULT_VALUE = "target/jacoco.exec";
  public static final String IT_REPORT_PATH_PROPERTY = "sonar.jacoco.itReportPath";
  public static final String IT_REPORT_PATH_DEFAULT_VALUE = "target/jacoco-it.exec";

  private final Settings settings;

  public JacocoConfiguration(Settings settings) {
    this.settings = settings;
  }

  public boolean isEnabled() {
    return StringUtils.isNotEmpty(getItReportPath()) || StringUtils.isNotEmpty(getReportPath());
  }

  public String getReportPath() {
    return settings.getString(REPORT_PATH_PROPERTY);
  }

  public String getItReportPath() {
    return settings.getString(IT_REPORT_PATH_PROPERTY);
  }

  public static List<PropertyDefinition> getPropertyDefinitions() {
    String subCategory = "JaCoCo";
    return ImmutableList.of(
        PropertyDefinition.builder(JacocoConfiguration.REPORT_PATH_PROPERTY)
            .defaultValue(JacocoConfiguration.REPORT_PATH_DEFAULT_VALUE)
            .category(CoreProperties.CATEGORY_JAVA)
            .subCategory(subCategory)
            .name("UT JaCoCo Report")
            .description("Path to the JaCoCo report file containing coverage data by unit tests. The path may be absolute or relative to the project base directory.")
            .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
            .build(),
        PropertyDefinition.builder(JacocoConfiguration.IT_REPORT_PATH_PROPERTY)
            .defaultValue(JacocoConfiguration.IT_REPORT_PATH_DEFAULT_VALUE)
            .category(CoreProperties.CATEGORY_JAVA)
            .subCategory(subCategory)
            .name("IT JaCoCo Report")
            .description("Path to the JaCoCo report file containing coverage data by integration tests. The path may be absolute or relative to the project base directory.")
            .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
            .build());
  }

}
