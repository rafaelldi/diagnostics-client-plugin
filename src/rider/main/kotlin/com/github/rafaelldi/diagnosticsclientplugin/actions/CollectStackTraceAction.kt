package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.StackTraceCollectionController
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.rd.util.launchOnUi
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.stacktrace.RiderStacktraceUtil

class CollectStackTraceAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val controller = project.service<StackTraceCollectionController>()
        project.lifetime.launchOnUi {
            val stackTrace = controller.collectStackTrace(pid)
            RiderStacktraceUtil.addAnalyzeExceptionTab(project, stackTrace, "Stack Trace for $pid")
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            event.presentation.isEnabled = false
        } else {
            event.presentation.isEnabled = project.solution.diagnosticsHostModel.processList.selected.value != null
        }
    }
}