package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopExportSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceExportSessionController
import com.intellij.openapi.project.Project

class StopExportTraceSessionAction : StopExportSessionAction() {
    override fun stopSession(pid: Int, project: Project) {
        TraceExportSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        TraceExportSessionController.getInstance(project).containsSession(pid)
}