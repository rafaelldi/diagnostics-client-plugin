package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceCollectionSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class TraceCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): TraceCollectionSessionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.traceCollectionSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: CollectTracesModel) {
        if (hostModel.traceCollectionSessions.contains(pid)) {
            sessionAlreadyExists(pid)
            return
        }

        val filePath = Path(model.path, model.filename).pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null
        val predefinedProvider = getPredefinedProviders(model)

        val session = TraceCollectionSession(
            filePath,
            model.profile.map(),
            model.providers,
            predefinedProvider,
            duration
        )

        try {
            hostModel.traceCollectionSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            sessionAlreadyExists(pid)
        }
    }

    fun stopSession(pid: Int) {
        hostModel.traceCollectionSessions.remove(pid)
    }

    private fun getPredefinedProviders(model: CollectTracesModel): List<PredefinedProvider> {
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

    private fun viewSession(pid: Int, session: TraceCollectionSession, lt: Lifetime) {
        if (session.duration != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (hostModel.traceCollectionSessions.containsKey(pid)) {
                    hostModel.traceCollectionSessions.remove(pid)
                }
            }
        }

        lt.bracketIfAlive(
            { sessionStarted(pid) },
            { sessionFinished(pid, session.filePath) }
        )
    }

    private fun sessionAlreadyExists(pid: Int) = Notification(
        "Diagnostics Client",
        "Traces collection session for $pid already exists",
        "",
        NotificationType.WARNING
    )
        .notify(project)

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "Traces collection started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int, filePath: String) = Notification(
        "Diagnostics Client",
        "Traces collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(RevealFileAction(filePath))
        .notify(project)
}