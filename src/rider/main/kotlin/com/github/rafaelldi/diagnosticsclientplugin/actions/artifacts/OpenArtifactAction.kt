package com.github.rafaelldi.diagnosticsclientplugin.actions.artifacts

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.RecentArtifactTab
import com.intellij.ide.actions.OpenFileAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.pathString

class OpenArtifactAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val tab = e.getData(RecentArtifactTab.RECENT_ARTIFACT_TAB) ?: return
        val selectedPath = tab.selectedArtifactPath ?: return
        if (selectedPath.exists()) {
            if ((selectedPath.extension == "csv" || selectedPath.extension == "json")) {
                OpenFileAction.openFile(selectedPath.pathString, project)
            }
        } else {
            tab.removeArtifact(selectedPath)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val tab = e.getData(RecentArtifactTab.RECENT_ARTIFACT_TAB)
        val selectedPath = tab?.selectedArtifactPath
        if (project == null || tab == null || selectedPath == null) {
            e.presentation.isEnabledAndVisible = false
        } else {
            e.presentation.isEnabledAndVisible = selectedPath.extension == "csv" || selectedPath.extension == "json"
        }
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}