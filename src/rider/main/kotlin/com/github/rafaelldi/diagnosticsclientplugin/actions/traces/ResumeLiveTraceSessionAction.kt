package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveTraceSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveTraceSessionTab.Companion.TRACE_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class ResumeLiveTraceSessionAction : ResumeLiveSessionAction<LiveTraceSession, LiveTraceSessionTab>() {
    override val tabDatKey = TRACE_MONITORING_TAB

    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            LiveTraceSessionController
                .getInstance(project)
                .startExistingSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.liveTraceSessions[pid]
}