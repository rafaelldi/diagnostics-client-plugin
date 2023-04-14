package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.xdebugger.attach.LocalAttachHost
import com.intellij.xdebugger.attach.XAttachDebuggerProvider

class AttachDebuggerAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val tab = e.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val selected = tab.selectedProcess ?: return

        runBackgroundableTask(DiagnosticsClientBundle.message("progress.attach.debugger"), e.project) {
            val processInfo =
                ProcessListUtil.getProcessList().firstOrNull { it.pid == selected.pid } ?: return@runBackgroundableTask
            val attachHost = LocalAttachHost.INSTANCE
            val dataHolder = UserDataHolderBase()
            val debugger = XAttachDebuggerProvider.EP.extensionList
                .filter { it.isAttachHostApplicable(attachHost) }
                .flatMap { it.getAvailableDebuggers(project, attachHost, processInfo, dataHolder) }
                .singleOrNull { it.debuggerDisplayName == ".NET Core Debugger" }
                ?: return@runBackgroundableTask

            debugger.attachDebugSession(project, attachHost, processInfo)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val tab = e.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            e.presentation.isEnabled = false
        } else {
            e.presentation.isEnabled = tab.selectedProcess != null
        }
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}