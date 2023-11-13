package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab
import com.intellij.openapi.project.Project

class PauseTraceSessionAction : PauseSessionAction<TraceSession, TraceProtocolSessionTab>() {
    override fun pauseSession(pid: Int, project: Project) {
        TraceSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        TraceSessionController.getInstance(project).getSession(pid)
}