/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
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
package org.sonar.java.checks;

import com.sonar.sslr.squid.checks.CheckMessagesVerifier;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squid.api.SourceFile;

import java.io.File;

public class CatchUsesExceptionWithContextCheckTest {

  public CatchUsesExceptionWithContextCheck check = new CatchUsesExceptionWithContextCheck();

  @Test
  public void detected() {
    SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/checks/CatchUsesExceptionWithContextCheck.java"), new VisitorsBridge(check));
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(4).withMessage("Either log or rethrow this exception.")
      .next().atLine(7)
      .next().atLine(11)
      .next().atLine(18)
      .next().atLine(22)
      .next().atLine(45)
      .next().atLine(62)
      .next().atLine(64)
      .next().atLine(75)
      .next().atLine(107)
      .next().atLine(113)
      .next().atLine(128)
      .next().atLine(129)
      .next().atLine(130)
      .next().atLine(131)
      .next().atLine(132)
      .next().atLine(145)
      .next().atLine(147)
      .noMore();
  }

}
