package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.CounterCollectionSessionController
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StopCollectingCountersAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        CounterCollectionSessionController.getInstance(project).stopSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            event.presentation.isEnabledAndVisible = false
        } else {
            val model = project.solution.diagnosticsHostModel
            val selected = model.processList.selected.value
            if (selected == null) {
                event.presentation.isEnabledAndVisible = false
            } else {
                event.presentation.isEnabledAndVisible = model.counterCollectionSessions.contains(selected)
            }
        }
    }
}