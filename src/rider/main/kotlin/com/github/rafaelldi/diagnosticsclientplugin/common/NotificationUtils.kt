package com.github.rafaelldi.diagnosticsclientplugin.common

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun fileDoesNotExist(path: String, project: Project) = Notification(
    "Diagnostics Client",
    DiagnosticsClientBundle.message("notifications.file.doesnt.exist"),
    DiagnosticsClientBundle.message("notifications.unable.to.find.file", path),
    NotificationType.ERROR
)
    .notify(project)
