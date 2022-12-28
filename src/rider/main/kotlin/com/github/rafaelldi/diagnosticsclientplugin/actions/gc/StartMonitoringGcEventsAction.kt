package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcEventsDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartMonitoringGcEventsAction : StartMonitoringAction<GcEventMonitoringSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = MonitorGcEventsDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model)
            GcEventMonitoringSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.gcEventMonitoringSessions[selected.pid]
}