package com.yuchesc.intellij.simple

import com.intellij.lang.Language
import com.intellij.openapi.diagnostic.Logger

class SimpleLanguage extends Language("Simple") {
  val logger = Logger.getInstance(classOf[SimpleLanguage])
  logger.warn("simple language")
}

object SimpleLanguage {
  val Instance = new SimpleLanguage
}