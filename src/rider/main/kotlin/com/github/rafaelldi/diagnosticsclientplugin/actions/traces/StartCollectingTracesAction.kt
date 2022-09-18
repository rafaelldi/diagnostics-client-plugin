package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.TraceCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.TracesSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.jetbrains.rider.projectView.solution

class StartCollectingTracesAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val dialog = CollectTracesDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TracesSettings.getInstance(project).update(model)
            TraceCollectionSessionController.getInstance(project).startSession(pid, model)
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
                event.presentation.isEnabledAndVisible = !model.traceCollectionSessions.contains(selected)
            }
        }
    }
}