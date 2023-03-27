package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.PersistentCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.PersistentSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class PersistentCounterSessionController(project: Project) :
    PersistentSessionController<PersistentCounterSession, CounterModel>(project) {
    companion object {
        fun getInstance(project: Project): PersistentCounterSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS
    override val canBeOpened = true
    override val sessions = project.solution.diagnosticsHostModel.persistentCounterSessions

    init {
        sessions.view(serviceLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: CounterModel): PersistentCounterSession {
        val filePath = calculateFilePath(model)
        val metrics = model.metrics.ifEmpty { null }
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return PersistentCounterSession(
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

    private fun calculateFilePath(model: CounterModel): String {
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
}