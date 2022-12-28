package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCollectingCountersAction : StartCollectingAction() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = CollectCountersDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterSettings.getInstance(project).update(model)
            CounterCollectionSessionController.getInstance(project).startSession(model)
        }
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.counterCollectionSessions.contains(selected.pid)
}