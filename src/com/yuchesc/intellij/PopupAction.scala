package com.yuchesc.intellij

import java.awt.{BorderLayout, Dimension}
import javax.swing.{JLabel, JPanel}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.popup.{JBPopup, JBPopupFactory}
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory

class PopupAction extends AnAction {
  def createPopup(): JBPopup = {
    val panel = new JPanel(new BorderLayout())
    val component = new JLabel()
    component.setText("Hello World!")
    JBPopupFactory.getInstance.createComponentPopupBuilder(panel, component)
      .setTitle("Recent Projects")
      .setFocusable(true)
      .setRequestFocus(false)
      .setMayBeParent(true)
      .setMovable(true)
      .setMinSize(new Dimension(200, 200))
      .createPopup
  }

  var currentPopup: Option[JBPopup] = None

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
