package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.intellij.openapi.project.Project

class ChartSessionListenerService(private val project: Project): ChartSessionListener {
    override fun sessionClosed(pid: Int) {
        val controller = LiveChartSessionController.getInstance(project)
        controller.closeSession(pid)
    }
}