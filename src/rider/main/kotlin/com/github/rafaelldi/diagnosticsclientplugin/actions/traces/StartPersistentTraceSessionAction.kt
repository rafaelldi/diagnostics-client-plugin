package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartPersistentSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.PersistentTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartPersistentTraceSessionAction : StartPersistentSessionAction() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = TraceDialog(project, selected, processes, true)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model, true)
            PersistentTraceSessionController.getInstance(project).startSession(model)
        }
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.persistentTraceSessions.contains(selected.pid)
}