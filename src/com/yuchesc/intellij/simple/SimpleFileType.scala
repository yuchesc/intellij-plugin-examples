package com.yuchesc.intellij.simple

import javax.swing.Icon

import com.intellij.openapi.fileTypes.LanguageFileType

class SimpleFileType extends LanguageFileType(SimpleLanguage.Instance) {
  override def getName: String = "Simple file"

  override def getDescription: String = "Simple language file"

  override def getIcon: Icon = SimpleIcons.File

  override def getDefaultExtension: String = "smpl"
}

object SimpleFileType {
  val Instance: SimpleFileType = new SimpleFileType
}