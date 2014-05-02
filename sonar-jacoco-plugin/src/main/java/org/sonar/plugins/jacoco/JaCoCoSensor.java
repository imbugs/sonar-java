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

import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.plugins.java.api.JavaResourceLocator;

import java.util.Collection;

public class JaCoCoSensor implements Sensor {

  private final JacocoConfiguration configuration;
  private final ResourcePerspectives perspectives;
  private final ModuleFileSystem fileSystem;
  private final PathResolver pathResolver;
  private final JavaResourceLocator javaResourceLocator;

  public JaCoCoSensor(JacocoConfiguration configuration, ResourcePerspectives perspectives, ModuleFileSystem fileSystem, PathResolver pathResolver,
                      JavaResourceLocator javaResourceLocator) {
    this.configuration = configuration;
    this.perspectives = perspectives;
    this.fileSystem = fileSystem;
    this.pathResolver = pathResolver;
    this.javaResourceLocator = javaResourceLocator;
  }

  /**
   * Should be executed after Surefire, which imports details of the tests.
   */
  @DependsUpon
  public String dependsOnSurefireSensors() {
    return "surefire-java";
  }

  public void analyse(Project project, SensorContext context) {
    new UnitTestsAnalyzer(perspectives).analyse(project, context);
  }

  public boolean shouldExecuteOnProject(Project project) {
    return configuration.isEnabled();
  }

  class UnitTestsAnalyzer extends AbstractAnalyzer {
    public UnitTestsAnalyzer(ResourcePerspectives perspectives) {
      super(perspectives, fileSystem, pathResolver, javaResourceLocator);
    }

    @Override
    protected String getReportPath(Project project) {
      return configuration.getReportPath();
    }

    @Override
    protected String getExcludes(Project project) {
      return null;
    }

    @Override
    protected void saveMeasures(SensorContext context, Resource resource, Collection<Measure> measures) {
      for (Measure measure : measures) {
        context.saveMeasure(resource, measure);
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
