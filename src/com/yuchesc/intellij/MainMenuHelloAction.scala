package com.yuchesc.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, PlatformDataKeys}
import com.intellij.openapi.ui.Messages

class MainMenuHelloAction extends AnAction {
  override def actionPerformed(e: AnActionEvent) {
    val project = e.getData(PlatformDataKeys.PROJECT_CONTEXT)
    val txt = Option(Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon))
    txt.foreach(tx => {
      Messages.showMessageDialog(project, "Hello, " + tx + "!\n I am glad to see you.", "Information", Messages.getInformationIcon)
    })
  }
}
