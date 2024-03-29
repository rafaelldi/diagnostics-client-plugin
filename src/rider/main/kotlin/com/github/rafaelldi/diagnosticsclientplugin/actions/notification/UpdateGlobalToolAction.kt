@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.actions.notification

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsToolService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchBackground
import com.intellij.openapi.rd.util.lifetime
import com.intellij.platform.ide.progress.withBackgroundProgress

class UpdateGlobalToolAction : NotificationAction(DiagnosticsClientBundle.message("notifications.update.global.tool")) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project ?: return
        project.lifetime.launchBackground {
            withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.update.global.tool")) {
                DiagnosticsToolService.getInstance(project).updateGlobalTool()
            }
        }
    }
}