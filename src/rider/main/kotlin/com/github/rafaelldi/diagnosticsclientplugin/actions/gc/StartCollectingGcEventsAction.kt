package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCollectingGcEventsAction : StartCollectingAction() {
    override fun startSession(pid: Int, project: Project) {
        val dialog = CollectGcEventsDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model)
            GcEventCollectionSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun containsSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.gcEventCollectionSessions.contains(pid)
}