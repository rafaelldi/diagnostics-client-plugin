package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartLiveGcEventSessionAction : StartLiveSessionAction<LiveGcEventSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = GcEventSessionDialog(project, selected, processes, false)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSettings.getInstance(project).update(model, false)
            LiveGcEventSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        LiveGcEventSessionController.getInstance(project).getSession(pid)
}