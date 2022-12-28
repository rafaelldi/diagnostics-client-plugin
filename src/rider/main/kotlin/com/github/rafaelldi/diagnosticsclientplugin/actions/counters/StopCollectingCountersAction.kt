package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopCollectingAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCollectingCountersAction : StopCollectingAction() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        CounterCollectionSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun containsSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.counterCollectionSessions.contains(selected.pid)
}