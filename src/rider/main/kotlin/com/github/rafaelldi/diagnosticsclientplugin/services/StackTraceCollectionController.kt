@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectStackTraceCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class StackTraceCollectionController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): StackTraceCollectionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    suspend fun collectStackTrace(pid: Int): String {
        val command = CollectStackTraceCommand(pid)
        return withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.stack.trace")) {
            return@withBackgroundProgress hostModel.collectStackTrace.startSuspending(command)
        }
    }
}