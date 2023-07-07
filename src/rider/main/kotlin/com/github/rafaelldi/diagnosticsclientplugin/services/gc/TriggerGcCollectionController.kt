package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
class TriggerGcCollectionController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): TriggerGcCollectionController = project.service()
    }

    fun triggerGc(pid: Int) {
        val model = DiagnosticsHost.getInstance(project).hostModel ?: return
        model.triggerGc.fire(pid)
    }
}