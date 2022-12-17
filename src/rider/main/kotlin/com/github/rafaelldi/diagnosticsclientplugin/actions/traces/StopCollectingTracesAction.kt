package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceCollectionSessionController
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingTracesAction : StopCollectingAction() {
    override fun stopSession(pid: Int, project: Project) {
        TraceCollectionSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.traceCollectionSessions.contains(pid)
}