package com.yuchesc.intellij.simple.psi

import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.SimpleLanguage

class SimpleElementType(debugName: String)
  extends IElementType(debugName, SimpleLanguage.Instance) {
}
