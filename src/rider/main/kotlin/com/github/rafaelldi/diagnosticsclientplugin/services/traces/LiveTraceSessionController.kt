package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.model.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class LiveTraceSessionController(project: Project) :
    LiveSessionController<LiveTraceSession, TraceSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveTraceSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val artifactType = TRACES

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.liveTraceSessions

    override fun addSessionTab(pid: Int, session: LiveTraceSession, sessionLifetime: Lifetime) {
        TraceSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: TraceSessionModel): LiveTraceSession {
        val providers = getPredefinedProviders(model)
        return LiveTraceSession(providers)
    }

    private fun getPredefinedProviders(model: TraceSessionModel): List<PredefinedProvider> {
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
}