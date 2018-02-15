package com.yuchesc.intellij.simple.psi

import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.SimpleLanguage

class SimpleTokenType(debugName: String)
  extends IElementType(debugName, SimpleLanguage.Instance) {
  override def toString: String = "SimpleTokenType." + super.toString
}
