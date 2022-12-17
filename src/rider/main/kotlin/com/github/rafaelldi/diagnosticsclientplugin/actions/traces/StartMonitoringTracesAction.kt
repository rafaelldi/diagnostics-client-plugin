package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorTracesDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartMonitoringTracesAction : StartMonitoringAction<TraceMonitoringSession>() {
    override fun startSession(pid: Int, project: Project) {
        val dialog = MonitorTracesDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model)
            TraceMonitoringSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.traceMonitoringSessions[pid]
}