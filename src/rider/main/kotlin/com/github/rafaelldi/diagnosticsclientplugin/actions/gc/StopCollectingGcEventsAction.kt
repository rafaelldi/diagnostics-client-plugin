package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventCollectionSessionController
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingGcEventsAction : StopCollectingAction() {
    override fun stopSession(pid: Int, project: Project) {
        GcEventCollectionSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.gcEventCollectionSessions.contains(pid)
}