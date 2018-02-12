package com.yuchesc.intellij

import com.intellij.notification.{Notification, NotificationType, Notifications}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

class ToolMenuHelloAction extends AnAction {
  override def actionPerformed(e: AnActionEvent) {
    Notifications.Bus.notify(
      new Notification("sample", "Hello Plugin!", "Hello! This is Sample Plugin.", NotificationType.INFORMATION)
    )
  }
}
