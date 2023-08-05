package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventExportSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ExportSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class GcEventExportSessionController(project: Project) :
    ExportSessionController<GcEventExportSession, GcEventSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): GcEventExportSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    override val artifactType = GC_EVENTS
    override val canBeOpened = true

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.gcEventExportSessions

    override fun createSession(model: GcEventSessionModel): GcEventExportSession {
        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return GcEventExportSession(duration, filePath)
    }
}