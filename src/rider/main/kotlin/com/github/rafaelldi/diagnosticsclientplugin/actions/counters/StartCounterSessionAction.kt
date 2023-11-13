package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartCounterSessionAction : StartSessionAction<CounterSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = CounterSessionDialog(project, selected, processes, false)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterSettings.getInstance(project).update(model, false)
            CounterSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        CounterSessionController.getInstance(project).getSession(pid)
}