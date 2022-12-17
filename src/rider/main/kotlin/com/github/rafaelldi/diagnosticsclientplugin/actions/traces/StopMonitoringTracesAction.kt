package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceMonitoringSessionController
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopMonitoringTracesAction : StopMonitoringAction<TraceMonitoringSession>() {
    override fun stopSession(pid: Int, project: Project) {
        TraceMonitoringSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.traceMonitoringSessions[pid]
}