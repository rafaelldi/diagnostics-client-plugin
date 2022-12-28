package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCollectingGcEventsAction : StartCollectingAction() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = CollectGcEventsDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model)
            GcEventCollectionSessionController.getInstance(project).startSession(model)
        }
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.gcEventCollectionSessions.contains(selected.pid)
}