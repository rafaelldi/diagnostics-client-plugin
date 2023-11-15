package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartExportSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventExportSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartExportGcEventSessionAction : StartExportSessionAction() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = GcEventSessionDialog(project, selected, processes, true)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model, true)
            GcEventExportSessionController.getInstance(project).startSession(model)
        }
    }

    override fun containsSession(pid: Int, project: Project) =
        GcEventExportSessionController.getInstance(project).containsSession(pid)
}