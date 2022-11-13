package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.services.StackTraceCollectionController
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
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        project.lifetime.launchOnUi {
            val stackTrace = StackTraceCollectionController.getInstance(project).collectStackTrace(pid)
            RiderStacktraceUtil.addAnalyzeExceptionTab(project, stackTrace, "Stack Trace for $pid")
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            event.presentation.isEnabled = tab.selectedProcessId != null
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}