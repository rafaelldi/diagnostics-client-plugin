package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class LiveChartSessionController(project: Project) :
    LiveSessionController<LiveChartSession, ChartModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveChartSessionController = project.service()
        private const val CHART = "Chart"
    }

    override val sessions = project.solution.diagnosticsHostModel.liveChartSessions

    init {
        sessions.view(serviceLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: ChartModel) = LiveChartSession()

    override fun sessionNotFound(pid: Int) = liveSessionNotFound(CHART, pid, project)
    override fun sessionStarted(pid: Int) = liveSessionStarted(CHART, pid, project)
    override fun sessionFinished(pid: Int) = liveSessionFinished(CHART, pid, project)
}