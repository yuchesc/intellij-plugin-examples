package com.yuchesc.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.Messages

class SelectedTextAction extends AnAction {

  // enable this action while editing text
  override def update(e: AnActionEvent): Unit =
    e.getPresentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null)

  override def actionPerformed(e: AnActionEvent) {
    // Get selected text
    val editor = e.getData(CommonDataKeys.EDITOR)
    val selectionModel = editor.getSelectionModel
    val selectedWord = selectionModel.getSelectedText

    // Get persistent data
    val project = e.getData(CommonDataKeys.PROJECT)
    val persistent = SamplePersistent.getInstance(project)
    Messages.showMessageDialog(project, s"selected:[$selectedWord]\nprevious:[${persistent.word}]", "Information", Messages.getInformationIcon)
    // Set persistent data
    persistent.setWord(selectedWord)
  }
}
