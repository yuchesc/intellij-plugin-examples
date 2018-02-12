package com.yuchesc.intellij

import com.intellij.openapi.components.{PersistentStateComponent, ServiceManager, State}
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

// Specify file name.
// @State(name = "Samplep", storages = Array(new Storage("sample-project.xml")))
// Write data in misc.xml as project-level.
@State(name = "Samplep")
class SamplePersistent extends PersistentStateComponent[SamplePersistent] {
  private var _word = ""

  def word: String = _word

  def setWord(word: String): Unit = {
    this._word = word
  }

  override def loadState(t: SamplePersistent): Unit =
    XmlSerializerUtil.copyBean(t, this)

  override def getState: SamplePersistent = this
}

object SamplePersistent {
  def getInstance(project: Project): SamplePersistent = ServiceManager.getService(project, classOf[SamplePersistent])
}

