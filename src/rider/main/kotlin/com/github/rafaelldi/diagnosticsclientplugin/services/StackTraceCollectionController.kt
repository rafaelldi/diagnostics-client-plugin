@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectStackTraceCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgressIndicator
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.solution

@Service
class StackTraceCollectionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        @JvmStatic
        fun getInstance(project: Project): StackTraceCollectionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    suspend fun collectStackTrace(pid: Int): String {
        val command = CollectStackTraceCommand(pid)
        return withBackgroundProgressIndicator(project, "Collecting stack trace") {
            return@withBackgroundProgressIndicator hostModel.collectStackTrace.startSuspending(command)
        }
    }
}