package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeLiveTraceSessionAction : ResumeLiveSessionAction<TraceProtocolSession, TraceProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceProtocolSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        TraceProtocolSessionController.getInstance(project).getSession(pid)
}