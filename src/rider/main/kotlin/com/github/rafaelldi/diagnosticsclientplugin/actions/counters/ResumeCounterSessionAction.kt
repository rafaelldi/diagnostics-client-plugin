package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeCounterSessionAction : ResumeSessionAction<CounterSession, CounterProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        CounterSessionController.getInstance(project).getSession(pid)
}