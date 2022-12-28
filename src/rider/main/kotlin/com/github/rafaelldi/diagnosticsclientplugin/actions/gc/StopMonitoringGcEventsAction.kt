package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopMonitoringGcEventsAction : StopMonitoringAction<GcEventMonitoringSession>() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        GcEventMonitoringSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun getSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.gcEventMonitoringSessions[selected.pid]
}