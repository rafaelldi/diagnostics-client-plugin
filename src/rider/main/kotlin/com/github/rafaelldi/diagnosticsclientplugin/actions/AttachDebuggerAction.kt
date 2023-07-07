package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
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
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE) ?: return

        val processNode = tree.selectedNode as? LocalProcessNode ?: return

        runBackgroundableTask(DiagnosticsClientBundle.message("progress.attach.debugger"), event.project) {
            val processInfo = ProcessListUtil.getProcessList().firstOrNull { it.pid == processNode.processId }
                ?: return@runBackgroundableTask
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

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}