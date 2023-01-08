package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopPersistentSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.PersistentGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopPersistentGcEventSessionAction : StopPersistentSessionAction() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        PersistentGcEventSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.persistentGcEventSessions.contains(selected.pid)
}