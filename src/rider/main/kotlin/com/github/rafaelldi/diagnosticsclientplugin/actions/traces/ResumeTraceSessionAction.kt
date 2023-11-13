package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeTraceSessionAction : ResumeSessionAction<TraceSession, TraceProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        TraceSessionController.getInstance(project).getSession(pid)
}