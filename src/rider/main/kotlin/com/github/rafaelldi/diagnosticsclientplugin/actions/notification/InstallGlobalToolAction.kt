@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.actions.notification

import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsToolService
import com.intellij.diagnostic.DiagnosticBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.rd.util.launchBackground
import com.jetbrains.rd.platform.util.lifetime

class InstallGlobalToolAction : NotificationAction(DiagnosticBundle.message("notifications.install.global.tool")) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project ?: return
        project.lifetime.launchBackground {
            withBackgroundProgress(project, DiagnosticBundle.message("progress.install.global.tool")) {
                DiagnosticsToolService.getInstance(project).installGlobalTool()
            }
        }
    }
}