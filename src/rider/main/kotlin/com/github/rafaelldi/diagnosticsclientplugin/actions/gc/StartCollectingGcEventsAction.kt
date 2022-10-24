package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventsSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartCollectingGcEventsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val dialog = CollectGcEventsDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventsSettings.getInstance(project).update(model)
            GcEventCollectionSessionController.getInstance(project).startSession(pid, model)
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
                event.presentation.isEnabledAndVisible = !model.gcEventsCollectionSessions.contains(selected)
            }
        }
    }
}