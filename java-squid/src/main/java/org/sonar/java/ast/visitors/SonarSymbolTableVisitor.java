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
package org.sonar.java.ast.visitors;

import com.sonar.sslr.api.AstNode;
import org.sonar.api.source.Symbol;
import org.sonar.api.source.Symbolizable;
import org.sonar.java.model.JavaTree;
import org.sonar.java.resolve.SemanticModel;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.EnumConstantTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LabeledStatementTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

public class SonarSymbolTableVisitor extends BaseTreeVisitor {

  private SemanticModel semanticModel;
  private Symbolizable symbolizable;
  private Symbolizable.SymbolTableBuilder symbolTableBuilder;
  private CompilationUnitTree outerClass;


  public SonarSymbolTableVisitor(Symbolizable symbolizable, SemanticModel semanticModel) {
    this.symbolizable = symbolizable;
    this.semanticModel = semanticModel;
    this.symbolTableBuilder = symbolizable.newSymbolTableBuilder();
  }

  @Override
  public void visitCompilationUnit(CompilationUnitTree tree) {
    if (outerClass == null) {
      outerClass = tree;
    }
    super.visitCompilationUnit(tree);

    if (tree.equals(outerClass)) {
      symbolizable.setSymbolTable(symbolTableBuilder.build());
    }
  }

  @Override
  public void visitClass(ClassTree tree) {
    if(tree.simpleName() != null) {
      createSymbol(tree, tree.simpleName());
    }
    super.visitClass(tree);
  }

  @Override
  public void visitVariable(VariableTree tree) {
    createSymbol(tree, tree.simpleName());
    super.visitVariable(tree);
  }

  @Override
  public void visitEnumConstant(EnumConstantTree tree) {
    createSymbol(tree, tree.simpleName());
    super.visitEnumConstant(tree);
  }

  @Override
  public void visitMethod(MethodTree tree) {
    createSymbol(tree, tree.simpleName());
    super.visitMethod(tree);
  }

  @Override
  public void visitLabeledStatement(LabeledStatementTree tree) {
    createSymbol(tree, tree.label());
    super.visitLabeledStatement(tree);
  }

  private void createSymbol(Tree tree, IdentifierTree identifier) {
    Symbol symbol = symbolTableBuilder.newSymbol(startOffsetFor(identifier), endOffsetFor(identifier));
    for (IdentifierTree usage : semanticModel.getUsages(semanticModel.getSymbol(tree))) {
      symbolTableBuilder.newReference(symbol, startOffsetFor(usage));
    }
  }

  private int startOffsetFor(IdentifierTree tree) {
    AstNode astNode = ((JavaTree) tree).getAstNode();
    return astNode.getFromIndex();
  }

  private int endOffsetFor(IdentifierTree tree) {
    AstNode astNode = ((JavaTree) tree).getAstNode();
    return astNode.getFromIndex() + astNode.getTokenValue().length();
  }

}
