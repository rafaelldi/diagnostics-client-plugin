package com.github.rafaelldi.diagnosticsclientplugin.actions.quick

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartLiveSessionQuickAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartLiveCounterSessionQuickAction : StartLiveSessionQuickAction<CounterProtocolSession>() {
    override fun getSession(pid: Int, project: Project) =
        CounterProtocolSessionController.getInstance(project).getSession(pid)

    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = CounterSessionDialog(project, selected, processes, false)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterSettings.getInstance(project).update(model, false)
            CounterProtocolSessionController.getInstance(project).startSession(model)
        }
    }

    override fun activateTab(pid: Int, project: Project) {
        CounterSessionTabManager.getInstance(project).activateTab(pid)
    }
}