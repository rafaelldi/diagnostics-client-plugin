package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab.Companion.TRACE_MONITORING_TAB
import com.intellij.openapi.project.Project

class PauseLiveTraceSessionAction : PauseLiveSessionAction<TraceProtocolSession, TraceProtocolSessionTab>() {
    override val tabDatKey = TRACE_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        TraceProtocolSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        TraceProtocolSessionController.getInstance(project).getSession(pid)
}