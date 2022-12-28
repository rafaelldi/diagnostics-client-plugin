package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingGcEventsAction : StopCollectingAction() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        GcEventCollectionSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.gcEventCollectionSessions.contains(selected.pid)
}