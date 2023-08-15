package com.github.rafaelldi.diagnosticsclientplugin.actions.stackTrace

import com.github.rafaelldi.diagnosticsclientplugin.services.StackTraceController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ChartProtocolSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rider.stacktrace.RiderStacktraceUtil

class CollectStackTraceAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = getProcessId(event) ?: return
        project.lifetime.launchOnUi {
            val stackTrace = StackTraceController.getInstance(project).collect(pid) ?: return@launchOnUi
            RiderStacktraceUtil.addAnalyzeExceptionTab(project, stackTrace, "Stack Trace for $pid")
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val pid = getProcessId(event)
        if (project == null || pid == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        event.presentation.isEnabledAndVisible = true
    }

    private fun getProcessId(event: AnActionEvent): Int? {
        val sessionPid = event.getData(ChartProtocolSessionTab.SESSION_PROCESS_ID)
        if (sessionPid != null) return sessionPid

        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (tree != null) return (tree.selectedNode as? LocalProcessNode)?.processId

        return null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}