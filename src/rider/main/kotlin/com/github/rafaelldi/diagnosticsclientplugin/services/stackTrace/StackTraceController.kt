@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services.stackTrace

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectStackTraceCommand
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext

@Service(Service.Level.PROJECT)
class StackTraceController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): StackTraceController = project.service()
    }

    suspend fun collect(pid: Int): String? {
        val model = DiagnosticsHost.getInstance(project).hostModel ?: return null
        val command = CollectStackTraceCommand(pid)
        return withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.stack.trace")) {
            withUiContext {
                model.collectStackTrace.startSuspending(command)
            }
        }
    }
}