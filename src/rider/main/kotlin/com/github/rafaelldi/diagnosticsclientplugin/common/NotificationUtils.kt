package com.github.rafaelldi.diagnosticsclientplugin.common

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun collectionSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type collection started 🚀",
    "Session for process $pid started",
    NotificationType.INFORMATION
)
    .notify(project)

fun collectionSessionFinished(type: String, pid: Int, filePath: String, openFile: Boolean, project: Project) =
    Notification(
        "Diagnostics Client",
        "$type collection finished 🎉",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(
            if (openFile) OpenFileAction(filePath)
            else RevealFileAction(filePath)
        )
        .notify(project)

fun collectionSessionAlreadyExists(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type collection session for $pid already exists",
    "",
    NotificationType.WARNING
)
    .notify(project)

fun monitoringSessionStarted(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring started",
    "Session for process $pid started",
    NotificationType.INFORMATION
)
    .notify(project)

fun monitoringSessionFinished(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring finished",
    "Session for process $pid finished",
    NotificationType.INFORMATION
)
    .notify(project)

fun monitoringSessionNotFound(type: String, pid: Int, project: Project) = Notification(
    "Diagnostics Client",
    "$type monitoring session for $pid not found",
    "",
    NotificationType.ERROR
)
    .notify(project)
