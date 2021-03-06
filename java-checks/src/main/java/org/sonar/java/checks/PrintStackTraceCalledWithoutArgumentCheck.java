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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.ast.parser.JavaGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1148",
  priority = Priority.CRITICAL,
  tags={"error-handling"})
@BelongsToProfile(title = "Sonar way", priority = Priority.CRITICAL)
public class PrintStackTraceCalledWithoutArgumentCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(JavaGrammar.PRIMARY);
  }

  @Override
  public void visitNode(AstNode node) {
    if (isPrintStackTraceCall(node)) {
      getContext().createLineViolation(this, "Use a logger to log this exception.", node);
    }
  }

  private static boolean isPrintStackTraceCall(AstNode node) {
    AstNode identifierSuffix = node.getFirstChild(JavaGrammar.IDENTIFIER_SUFFIX);

    return identifierSuffix != null &&
      hasArgumentIdentifierSuffix(identifierSuffix) &&
      isPrintStackTraceQualifiedIdentifier(node.getFirstChild(JavaGrammar.QUALIFIED_IDENTIFIER));
  }

  private static boolean hasArgumentIdentifierSuffix(AstNode node) {
    return node.hasDirectChildren(JavaGrammar.ARGUMENTS);
  }

  private static boolean isPrintStackTraceQualifiedIdentifier(AstNode node) {
    return "printStackTrace".equals(node.getLastChild().getTokenOriginalValue());
  }

}
