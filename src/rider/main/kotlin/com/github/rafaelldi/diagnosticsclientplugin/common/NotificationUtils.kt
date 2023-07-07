package com.github.rafaelldi.diagnosticsclientplugin.common

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun exportSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.collection.started", type),
    DiagnosticsClientBundle.message("notifications.session.for.process.started", pid),
    NotificationType.INFORMATION
)
    .notify(project)

fun exportSessionFinished(type: String, pid: Int, filePath: String, openFile: Boolean, project: Project) =
    Notification(
        "Diagnostics Client",
        DiagnosticsClientBundle.message("notifications.collection.finished", type),
        DiagnosticsClientBundle.message("notifications.session.for.process.finished", pid),
        NotificationType.INFORMATION
    )
        .addAction(
            if (openFile) OpenFileAction(filePath)
            else RevealFileAction(filePath)
        )
        .notify(project)

fun exportSessionAlreadyExists(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.collection.session.exists", type, pid),
    "",
    NotificationType.WARNING
)
    .notify(project)

fun liveSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.monitoring.started", type),
    DiagnosticsClientBundle.message("notifications.session.for.process.started", pid),
    NotificationType.INFORMATION
)
    .notify(project)

fun liveSessionFinished(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.monitoring.finished", type),
    DiagnosticsClientBundle.message("notifications.session.for.process.finished", pid),
    NotificationType.INFORMATION
)
    .notify(project)

fun liveSessionNotFound(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.monitoring.session.not.found", type, pid),
    "",
    NotificationType.ERROR
)
    .notify(project)

fun fileDoesNotExist(path: String, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.file.doesnt.exist"),
    DiagnosticsClientBundle.message("notifications.unable.to.find.file", path),
    NotificationType.ERROR
)
    .notify(project)
