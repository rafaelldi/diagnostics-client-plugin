package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.services.StackTraceController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
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
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE) ?: return

        val processNode = tree.selectedNode as? LocalProcessNode ?: return
        val pid = processNode.processId

        project.lifetime.launchOnUi {
            val stackTrace = StackTraceController.getInstance(project).collect(pid) ?: return@launchOnUi
            RiderStacktraceUtil.addAnalyzeExceptionTab(project, stackTrace, "Stack Trace for $pid")
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (project == null || tree == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val processNode = tree.selectedNode as? LocalProcessNode
        if (processNode == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        event.presentation.isEnabledAndVisible = true
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}