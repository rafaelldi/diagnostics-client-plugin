package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopPersistentSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.PersistentCounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopPersistentCounterSessionAction : StopPersistentSessionAction() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        PersistentCounterSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.persistentCounterSessions.contains(selected.pid)
}