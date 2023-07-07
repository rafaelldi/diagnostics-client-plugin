package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.PersistentTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.model.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ExportSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class ExportTraceSessionController(project: Project) :
    ExportSessionController<PersistentTraceSession, TraceSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): ExportTraceSessionController = project.service()
        private const val TRACES = "Traces"
    }

    override val artifactType = TRACES
    override val canBeOpened = false

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.persistentTraceSessions

    override fun createSession(model: TraceSessionModel): PersistentTraceSession {
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