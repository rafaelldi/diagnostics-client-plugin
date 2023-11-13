package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.SessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service(Service.Level.PROJECT)
class TraceSessionController(project: Project) : SessionController<TraceSession, TraceSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): TraceSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val artifactType = TRACES

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.traceSessions

    override fun addSessionTab(pid: Int, session: TraceSession, sessionLifetime: Lifetime) {
        TraceSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: TraceSessionModel): TraceSession {
        val providers = getPredefinedProviders(model)
        return TraceSession(providers)
    }

    private fun getPredefinedProviders(model: TraceSessionModel): List<PredefinedProvider> {
        val providers = buildList {
            if (model.http)
                add(PredefinedProvider.Http)
            if (model.aspNet)
                add(PredefinedProvider.AspNet)
            if (model.ef)
                add(PredefinedProvider.EF)
            if (model.exceptions)
                add(PredefinedProvider.Exceptions)
            if (model.threads)
                add(PredefinedProvider.Threads)
            if (model.contentions)
                add(PredefinedProvider.Contentions)
            if (model.tasks)
                add(PredefinedProvider.Tasks)
            if (model.loader)
                add(PredefinedProvider.Loader)
        }

        return providers
    }
}