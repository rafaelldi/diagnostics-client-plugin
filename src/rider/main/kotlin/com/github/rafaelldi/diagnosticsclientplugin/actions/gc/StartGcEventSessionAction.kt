package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartGcEventSessionAction : StartSessionAction<GcEventSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = GcEventSessionDialog(project, selected, processes, false)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model, false)
            GcEventSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        GcEventSessionController.getInstance(project).getSession(pid)
}