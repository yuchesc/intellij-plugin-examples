package com.yuchesc.intellij.simple.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.yuchesc.intellij.simple.{SimpleFileType, SimpleLanguage}

class SimpleFile(fileViewProvider: FileViewProvider)
  extends PsiFileBase(fileViewProvider, SimpleLanguage.Instance) {
  override def getFileType: FileType = SimpleFileType.Instance

  override def toString: String = "Simple File"
}
