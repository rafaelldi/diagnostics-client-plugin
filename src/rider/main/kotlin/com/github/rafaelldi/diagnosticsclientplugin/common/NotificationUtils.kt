package com.github.rafaelldi.diagnosticsclientplugin.common

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun persistentSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type collection started",
    "Session for process $pid started",
    NotificationType.INFORMATION
)
    .notify(project)

fun persistentSessionFinished(type: String, pid: Int, filePath: String, openFile: Boolean, project: Project) =
    Notification(
        "Diagnostics Client",
        "$type collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(
            if (openFile) OpenFileAction(filePath)
            else RevealFileAction(filePath)
        )
        .notify(project)

fun persistentSessionAlreadyExists(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type collection session for $pid already exists",
    "",
    NotificationType.WARNING
)
    .notify(project)

fun liveSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring started",
    "Session for process $pid started",
    NotificationType.INFORMATION
)
    .notify(project)

fun liveSessionFinished(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring finished",
    "Session for process $pid finished",
    NotificationType.INFORMATION
)
    .notify(project)

fun liveSessionNotFound(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring session for $pid not found",
    "",
    NotificationType.ERROR
)
    .notify(project)
