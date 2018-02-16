package com.yuchesc.intellij.simple

import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.{FileViewProvider, PsiElement, PsiFile}
import com.intellij.psi.tree.{IFileElementType, TokenSet}
import com.yuchesc.intellij.simple.psi.{SimpleFile, SimpleTypes}

/*
 * You may need plugins: PsiViewer
 */
class SimpleParserDefinition extends ParserDefinition {
  val File: IFileElementType = new IFileElementType(SimpleLanguage.Instance)
  override def getFileNodeType: IFileElementType = File

  override def createElement(astNode: ASTNode): PsiElement = SimpleTypes.Factory.createElement(astNode)

  override def createFile(fileViewProvider: FileViewProvider): PsiFile = new SimpleFile(fileViewProvider)

  override def getStringLiteralElements: TokenSet = TokenSet.EMPTY

  override def spaceExistanceTypeBetweenTokens(astNode: ASTNode, astNode1: ASTNode): ParserDefinition.SpaceRequirements = SpaceRequirements.MAY

  override def createLexer(project: Project): Lexer = new SimpleLexerAdapter()

  val Comments: TokenSet = TokenSet.create(SimpleTypes.COMMENT)
  override def getCommentTokens: TokenSet = Comments

  override def createParser(project: Project): PsiParser = new SimpleParser
}
