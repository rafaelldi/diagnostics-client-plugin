package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.intellij.openapi.project.Project

class CounterSessionListenerService(private val project: Project) : CounterSessionListener {
    override fun sessionClosed(pid: Int) {
        val controller = CounterLiveSessionController.getInstance(project)
        controller.closeSession(pid)
    }
}