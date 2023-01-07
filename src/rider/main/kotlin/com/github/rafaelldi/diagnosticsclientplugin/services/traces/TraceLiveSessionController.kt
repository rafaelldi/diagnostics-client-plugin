package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class TraceLiveSessionController(project: Project) :
    LiveSessionController<TraceMonitoringSession, TraceModel>(project) {
    companion object {
        fun getInstance(project: Project): TraceLiveSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val sessions = project.solution.diagnosticsHostModel.traceMonitoringSessions

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: TraceModel): TraceMonitoringSession {
        val providers = getPredefinedProviders(model)
        return TraceMonitoringSession(providers)
    }

    private fun getPredefinedProviders(model: TraceModel): List<PredefinedProvider> {
        val providers = mutableListOf<PredefinedProvider>()

        if (model.http)
            providers.add(PredefinedProvider.Http)
        if (model.aspNet)
            providers.add(PredefinedProvider.AspNet)
        if (model.ef)
            providers.add(PredefinedProvider.EF)
        if (model.exceptions)
            providers.add(PredefinedProvider.Exceptions)
        if (model.threads)
            providers.add(PredefinedProvider.Threads)
        if (model.contentions)
            providers.add(PredefinedProvider.Contentions)
        if (model.tasks)
            providers.add(PredefinedProvider.Tasks)
        if (model.loader)
            providers.add(PredefinedProvider.Loader)

        return providers
    }

    override fun sessionNotFound(pid: Int) = monitoringSessionNotFound(TRACES, pid, project)
    override fun sessionStarted(pid: Int) = monitoringSessionStarted(TRACES, pid, project)
    override fun sessionFinished(pid: Int) = monitoringSessionFinished(TRACES, pid, project)
}