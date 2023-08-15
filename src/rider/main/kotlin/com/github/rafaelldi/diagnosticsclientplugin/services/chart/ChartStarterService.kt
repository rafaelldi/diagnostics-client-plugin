package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.settings.DiagnosticsClientSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.openapi.wm.ToolWindowManager

@Service(Service.Level.PROJECT)
class ChartStarterService(private val project: Project) {
    companion object {
        private const val DPA_RUNNER = "JetBrains.DPA.Runner"

        fun getInstance(project: Project) = project.service<ChartStarterService>()
    }

    suspend fun startChartsForProcess(process: String) {
        val settings = DiagnosticsClientSettings.getInstance()
        if (!settings.showChartsOnStart) return

        val host = DiagnosticsHost.getInstance(project)
        if (!host.isConnected) return

        val processPath =
            if (process.contains(DPA_RUNNER)) {
                val segments = process.split(' ')
                if (segments.size < 6) null
                else segments[5]
            } else {
                process
            }
                ?: return

        val dotnetProcess = host.findProcess(processPath) ?: return
        val model = ChartSessionModel(dotnetProcess, StoppingType.Manually, 0)
        withUiContext {
            val toolWindow = ToolWindowManager.getInstance(project)
                .getToolWindow(DiagnosticsToolWindowFactory.DIAGNOSTICS_CLIENT_TOOL_WINDOW)
            toolWindow?.show()
            ChartProtocolSessionController.getInstance(project).startSession(model)
        }
    }
}