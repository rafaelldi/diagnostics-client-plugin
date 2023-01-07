package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TracePersistentSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingTracesAction : StopCollectingAction() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        TracePersistentSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.traceCollectionSessions.contains(selected.pid)
}