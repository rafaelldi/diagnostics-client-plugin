package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCollectingTracesAction : StartCollectingAction() {
    override fun startSession(pid: Int, project: Project) {
        val dialog = CollectTracesDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model)
            TraceCollectionSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun containsSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.traceCollectionSessions.contains(pid)
}