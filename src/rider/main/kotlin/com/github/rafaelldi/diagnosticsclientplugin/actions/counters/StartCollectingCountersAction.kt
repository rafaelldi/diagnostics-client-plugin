package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.CounterCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.CountersSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.jetbrains.rider.projectView.solution

class StartCollectingCountersAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val dialog = CollectCountersDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CountersSettings.getInstance(project).update(model)
            val controller = project.service<CounterCollectionSessionController>()
            controller.startSession(pid, model)
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
                event.presentation.isEnabledAndVisible = !model.counterCollectionSessions.contains(selected)
            }
        }
    }
}