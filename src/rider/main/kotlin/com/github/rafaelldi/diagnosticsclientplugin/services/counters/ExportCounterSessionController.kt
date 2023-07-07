package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.PersistentCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ExportSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class ExportCounterSessionController(project: Project) :
    ExportSessionController<PersistentCounterSession, CounterSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): ExportCounterSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS
    override val canBeOpened = true

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.persistentCounterSessions

    override fun createSession(model: CounterSessionModel): PersistentCounterSession {
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

    private fun calculateFilePath(model: CounterSessionModel): String {
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