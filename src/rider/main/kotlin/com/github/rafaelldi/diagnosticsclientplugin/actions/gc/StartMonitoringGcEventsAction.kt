package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcEventsDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventsSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartMonitoringGcEventsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val dialog = MonitorGcEventsDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventsSettings.getInstance(project).update(model)
            GcEventMonitoringSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            event.presentation.isEnabled = false
        } else {
            val model = project.solution.diagnosticsHostModel
            val selected = model.processList.selected.value
            if (selected == null) {
                event.presentation.isEnabled = false
            } else {
                val session = model.gcEventsMonitoringSessions[selected]
                val isActive = session?.active?.valueOrNull ?: false
                event.presentation.isEnabledAndVisible = !isActive
            }
        }
    }
}