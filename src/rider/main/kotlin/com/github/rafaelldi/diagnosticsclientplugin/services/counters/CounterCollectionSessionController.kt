package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionAlreadyExists
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterCollectionSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.CollectionSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class CounterCollectionSessionController(project: Project) :
    CollectionSessionController<CounterCollectionSession, CollectCountersModel>(project) {
    companion object {
        fun getInstance(project: Project): CounterCollectionSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val sessions = project.solution.diagnosticsHostModel.counterCollectionSessions

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: CollectCountersModel): CounterCollectionSession {
        val filePath = calculateFilePath(model)
        val metrics = model.metrics.ifEmpty { null }
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return CounterCollectionSession(
            model.format.map(),
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
            duration,
            filePath
        )
    }

    private fun calculateFilePath(model: CollectCountersModel): String {
        val filename = when (model.format) {
            CounterFileFormat.Csv -> {
                if (model.filename.endsWith(".csv"))
                    model.filename
                else
                    "${model.filename}.csv"
            }

            CounterFileFormat.Json -> {
                if (model.filename.endsWith(".json"))
                    model.filename
                else
                    "${model.filename}.json"
            }
        }

        return Path(model.path, filename).pathString
    }

    override fun sessionAlreadyExists(pid: Int) = collectionSessionAlreadyExists(COUNTERS, pid, project)
    override fun sessionStarted(pid: Int) = collectionSessionStarted(COUNTERS, pid, project)
    override fun sessionFinished(pid: Int, session: CounterCollectionSession) =
        collectionSessionFinished(COUNTERS, pid, session.filePath, true, project)
}