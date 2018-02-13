package com.yuchesc.intellij

import java.awt.{Dimension, FlowLayout}
import javax.swing.{JLabel, JPanel}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.popup.{JBPopup, JBPopupFactory}

class PopupAction extends AnAction {
  def createPopup(): JBPopup = {
    val panel = new JPanel(new FlowLayout())
    val label = new JLabel("Hello World!!")
    panel.add(label)
    JBPopupFactory.getInstance.createComponentPopupBuilder(panel, null)
      .setTitle("Popup Title")
      .setFocusable(false)
      .setRequestFocus(false)
      .setMayBeParent(true)
      .setMovable(true)
      .setMinSize(new Dimension(200, 200))
      .setCancelKeyEnabled(true)
      .createPopup
  }

  var currentPopup: Option[JBPopup] = None

  // enable this action while editing text
  override def update(e: AnActionEvent): Unit =
    e.getPresentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null)

  override def actionPerformed(e: AnActionEvent): Unit = {
    val editor = e.getData(CommonDataKeys.EDITOR)

    currentPopup.foreach { p =>
      p.setUiVisible(false)
      p.dispose()
    }

    currentPopup = Option(createPopup())
    currentPopup.foreach(_.showInBestPositionFor(editor))
  }
}
