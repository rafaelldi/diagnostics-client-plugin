package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.intellij.openapi.project.Project

class TraceSessionListenerService(private val project: Project) : TraceSessionListener {
    override fun sessionClosed(pid: Int) {
        val controller = TraceMonitoringSessionController.getInstance(project)
        controller.closeSession(pid)
    }
}