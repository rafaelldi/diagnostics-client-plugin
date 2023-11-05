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

class InstallGlobalToolAction : NotificationAction(DiagnosticsClientBundle.message("notifications.install.global.tool")) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project ?: return
        project.lifetime.launchBackground {
            withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.install.global.tool")) {
                DiagnosticsToolService.getInstance(project).installGlobalTool()
            }
        }
    }
}