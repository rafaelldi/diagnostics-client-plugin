package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceLiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceMonitoringTab.Companion.TRACE_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopTraceMonitoringSessionAction :
    StopMonitoringSessionAction<TraceMonitoringSession, TraceMonitoringTab>() {
    override val tabDatKey = TRACE_MONITORING_TAB

    override fun stopSession(pid: Int, project: Project) {
        TraceLiveSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.traceMonitoringSessions[pid]
}