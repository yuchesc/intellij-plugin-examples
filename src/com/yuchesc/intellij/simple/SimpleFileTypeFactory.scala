package com.yuchesc.intellij.simple

import com.intellij.openapi.fileTypes.{FileTypeConsumer, FileTypeFactory}

class SimpleFileTypeFactory extends FileTypeFactory {
  override def createFileTypes(fileTypeConsumer: FileTypeConsumer): Unit =
    fileTypeConsumer.consume(SimpleFileType.Instance, "smpl")
}
