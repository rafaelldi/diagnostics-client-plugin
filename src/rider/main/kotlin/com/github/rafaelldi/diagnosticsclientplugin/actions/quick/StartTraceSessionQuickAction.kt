package com.github.rafaelldi.diagnosticsclientplugin.actions.quick

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartSessionQuickAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartTraceSessionQuickAction : StartSessionQuickAction<TraceSession>() {
    override fun getSession(pid: Int, project: Project) =
        TraceSessionController.getInstance(project).getSession(pid)

    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = TraceSessionDialog(project, selected, processes, false)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model, false)
            TraceSessionController.getInstance(project).startSession(model)
        }
    }

    override fun activateTab(pid: Int, project: Project) {
        TraceSessionTabManager.getInstance(project).activateTab(pid)
    }
}