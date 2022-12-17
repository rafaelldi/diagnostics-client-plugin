package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterCollectionSessionController
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingCountersAction : StopCollectingAction() {
    override fun stopSession(pid: Int, project: Project) {
        CounterCollectionSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.counterCollectionSessions.contains(pid)
}