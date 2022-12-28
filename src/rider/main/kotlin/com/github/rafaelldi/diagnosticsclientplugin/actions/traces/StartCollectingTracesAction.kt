package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCollectingTracesAction : StartCollectingAction() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = CollectTracesDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model)
            TraceCollectionSessionController.getInstance(project).startSession(model)
        }
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.traceCollectionSessions.contains(selected.pid)
}