package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveTraceSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveTraceSessionTab.Companion.TRACE_MONITORING_TAB
import com.intellij.openapi.project.Project

class PauseLiveTraceSessionAction : PauseLiveSessionAction<LiveTraceSession, LiveTraceSessionTab>() {
    override val tabDatKey = TRACE_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        LiveTraceSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        LiveTraceSessionController.getInstance(project).getSession(pid)
}