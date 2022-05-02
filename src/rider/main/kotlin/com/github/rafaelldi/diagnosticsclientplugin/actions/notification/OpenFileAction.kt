package com.github.rafaelldi.diagnosticsclientplugin.actions.notification

import com.intellij.ide.actions.OpenFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenFileAction(private val filePath: String) : NotificationAction("Open file") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project ?: return
        OpenFileAction.openFile(filePath, project)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}