package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterExportSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ExportSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class CounterExportSessionController(project: Project) :
    ExportSessionController<CounterExportSession, CounterSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): CounterExportSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS
    override val canBeOpened = true

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.counterExportSessions

    override fun createSession(model: CounterSessionModel): CounterExportSession {
        val filePath = calculateFilePath(model)
        val metrics = model.metrics.ifEmpty { null }
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return CounterExportSession(
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