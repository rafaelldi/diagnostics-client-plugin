package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceMonitoringTab.Companion.TRACE_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class PauseLiveTraceSessionAction : PauseLiveSessionAction<LiveTraceSession, TraceMonitoringTab>() {
    override val tabDatKey = TRACE_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        LiveTraceSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.liveTraceSessions[pid]
}