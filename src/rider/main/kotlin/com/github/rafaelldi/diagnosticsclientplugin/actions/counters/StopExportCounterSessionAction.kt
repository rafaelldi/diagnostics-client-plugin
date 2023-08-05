package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopExportSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterExportSessionController
import com.intellij.openapi.project.Project

class StopExportCounterSessionAction : StopExportSessionAction() {
    override fun stopSession(pid: Int, project: Project) {
        CounterExportSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        CounterExportSessionController.getInstance(project).containsSession(pid)
}