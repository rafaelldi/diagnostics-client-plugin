package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeLiveCounterSessionAction : ResumeLiveSessionAction<CounterProtocolSession, CounterProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterProtocolSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        CounterProtocolSessionController.getInstance(project).getSession(pid)
}