package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectStackTraceCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.util.idea.runUnderProgress

@Service
class StackTraceCollectionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    fun collectStackTrace(pid: Int): String? {
        val command = CollectStackTraceCommand(pid)

        return hostModel.collectStackTrace
            .runUnderProgress(
                command,
                project,
                "Collecting stack trace",
                false
            )
    }
}