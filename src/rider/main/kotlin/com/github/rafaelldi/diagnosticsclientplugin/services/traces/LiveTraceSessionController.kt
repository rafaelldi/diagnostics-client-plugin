package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class LiveTraceSessionController(project: Project) :
    LiveSessionController<LiveTraceSession, TraceModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveTraceSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val sessions = project.solution.diagnosticsHostModel.liveTraceSessions

    init {
        sessions.view(serviceLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: TraceModel): LiveTraceSession {
        val providers = getPredefinedProviders(model)
        return LiveTraceSession(providers)
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

    override fun sessionNotFound(pid: Int) = liveSessionNotFound(TRACES, pid, project)
    override fun sessionStarted(pid: Int) = liveSessionStarted(TRACES, pid, project)
    override fun sessionFinished(pid: Int) = liveSessionFinished(TRACES, pid, project)

    class TraceSessionListenerImpl(private val project: Project) : TraceSessionListener {
        override fun sessionClosed(pid: Int) {
            getInstance(project).closeSession(pid)
        }
    }
}