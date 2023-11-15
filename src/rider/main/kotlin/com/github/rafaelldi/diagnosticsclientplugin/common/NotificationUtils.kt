package com.github.rafaelldi.diagnosticsclientplugin.common

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

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

fun fileDoesNotExist(path: String, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.file.doesnt.exist"),
    DiagnosticsClientBundle.message("notifications.unable.to.find.file", path),
    NotificationType.ERROR
)
    .notify(project)
