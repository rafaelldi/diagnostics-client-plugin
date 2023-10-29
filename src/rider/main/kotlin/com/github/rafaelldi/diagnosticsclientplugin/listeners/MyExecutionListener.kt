package com.github.rafaelldi.diagnosticsclientplugin.listeners

import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartStarterService
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchBackground
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rider.run.TerminalProcessHandler

class MyExecutionListener(private val project: Project) : ExecutionListener {
    companion object {
        private const val DEBUG_EXECUTOR_ID = "Debug"
    }

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (handler.isProcessTerminating || handler.isProcessTerminated) return
        if (executorId == DEBUG_EXECUTOR_ID) return

        (handler as? TerminalProcessHandler)?.let {
            project.lifetime.launchBackground {
                ChartStarterService.getInstance(project).startChartsForProcess(it.commandLine)
            }
        }
    }
}