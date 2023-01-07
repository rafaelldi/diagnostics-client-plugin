package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.intellij.openapi.project.Project

class GcEventSessionListenerService(private val project: Project) : GcEventSessionListener {
    override fun sessionClosed(pid: Int) {
        val controller = GcEventLiveSessionController.getInstance(project)
        controller.closeSession(pid)
    }
}