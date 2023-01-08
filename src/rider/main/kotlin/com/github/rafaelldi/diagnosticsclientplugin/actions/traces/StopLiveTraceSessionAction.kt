package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopLiveTraceSessionAction : StopLiveSessionAction<LiveTraceSession>() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        LiveTraceSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun getSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.liveTraceSessions[selected.pid]
}