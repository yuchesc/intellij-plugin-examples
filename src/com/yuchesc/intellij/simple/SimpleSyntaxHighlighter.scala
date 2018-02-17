package com.yuchesc.intellij.simple

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.{HighlighterColors, DefaultLanguageHighlighterColors => DefaultColors}
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.psi.SimpleTypes
import com.intellij.openapi.editor.colors.TextAttributesKey.{createTextAttributesKey => createKey}
import com.intellij.psi.TokenType

class SimpleSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getTokenHighlights(iElementType: IElementType): Array[TextAttributesKey] =
    SimpleSyntaxHighlighter.KeysMap.getOrElse(iElementType, SimpleSyntaxHighlighter.EmptyKeys)

  override def getHighlightingLexer: Lexer = new SimpleLexerAdapter
}

object SimpleSyntaxHighlighter {
  val Key: TextAttributesKey = createKey("SIMPLE_KEY", DefaultColors.KEYWORD)
  val Separator: TextAttributesKey = createKey("SIMPLE_SEPARATOR", DefaultColors.OPERATION_SIGN)
  val Value: TextAttributesKey = createKey("SIMPLE_VALUE", DefaultColors.STRING)

  val KeysMap: Map[IElementType, Array[TextAttributesKey]] = Map(
    SimpleTypes.SEPARATOR -> Array(Separator),
    SimpleTypes.KEY -> Array[TextAttributesKey](Key),
    SimpleTypes.VALUE -> Array[TextAttributesKey](Value),
    TokenType.BAD_CHARACTER -> Array[TextAttributesKey](createKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)),
    SimpleTypes.COMMENT -> Array(createKey("SIMPLE_COMMENT", DefaultColors.LINE_COMMENT))
  )
  val EmptyKeys: Array[TextAttributesKey] = Array.empty[TextAttributesKey]
}