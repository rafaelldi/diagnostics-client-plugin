package com.github.rafaelldi.diagnosticsclientplugin.actions.notification

import com.intellij.ide.actions.RevealFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlin.io.path.Path

class RevealFileAction(private val path: String) : NotificationAction("Show file") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        RevealFileAction.openFile(Path(path))
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null && RevealFileAction.isSupported()
    }
}