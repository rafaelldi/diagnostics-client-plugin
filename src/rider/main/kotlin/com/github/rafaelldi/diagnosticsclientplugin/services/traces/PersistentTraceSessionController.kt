package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionAlreadyExists
import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.PersistentTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.PersistentSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class PersistentTraceSessionController(project: Project) :
    PersistentSessionController<PersistentTraceSession, TraceModel>(project) {
    companion object {
        fun getInstance(project: Project): PersistentTraceSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val sessions = project.solution.diagnosticsHostModel.persistentTraceSessions

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: TraceModel): PersistentTraceSession {
        val filePath = Path(model.path, model.filename).pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null
        val predefinedProvider = getPredefinedProviders(model)

        return PersistentTraceSession(
            model.profile.map(),
            model.providers,
            predefinedProvider,
            duration,
            filePath
        )
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

    override fun sessionAlreadyExists(pid: Int) = persistentSessionAlreadyExists(TRACES, pid, project)
    override fun sessionStarted(pid: Int) = persistentSessionStarted(TRACES, pid, project)
    override fun sessionFinished(pid: Int, session: PersistentTraceSession) =
        persistentSessionFinished(TRACES, pid, session.filePath, false, project)

}