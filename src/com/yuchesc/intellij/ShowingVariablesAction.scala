package com.yuchesc.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages

class ShowingVariablesAction extends AnAction {
  val logger = Logger.getInstance(classOf[ShowingVariablesAction])

  // enable this action while editing text
  override def update(e: AnActionEvent): Unit =
    e.getPresentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null)

  override def actionPerformed(e: AnActionEvent) {
    logger.info("aiueo")
    // Get selected text
    val editor = e.getData(CommonDataKeys.EDITOR)
    val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
    val project = e.getData(CommonDataKeys.PROJECT)
    var text = "base path: " + project.getBasePath
    text = "\ncurrent dir: " + file.getParent

    val manager = ProjectRootManager.getInstance(project)
    val vFiles = manager.getContentRoots()
    text += "\ncontent roots: " + vFiles.map(_.getPath).mkString(",")
    val sFiles = manager.getContentSourceRoots
    text += "\nsource roots: " + sFiles.map(_.getPath).mkString(",")
    Messages.showMessageDialog(project, text, "Information", Messages.getInformationIcon)
  }
}
