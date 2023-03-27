package com.github.rafaelldi.diagnosticsclientplugin.actions.artifacts

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.RecentArtifactTab
import com.intellij.ide.actions.RevealFileAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlin.io.path.exists

class ShowArtifactInFolderAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val tab = e.getData(RecentArtifactTab.RECENT_ARTIFACT_TAB) ?: return
        val selectedPath = tab.selectedArtifactPath ?: return
        if (selectedPath.exists()) {
            RevealFileAction.openFile(selectedPath)
        } else {
            tab.removeArtifact(selectedPath)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val tab = e.getData(RecentArtifactTab.RECENT_ARTIFACT_TAB)
        if (project == null || tab == null) {
            e.presentation.isEnabled = false
        } else {
            e.presentation.isEnabled = tab.selectedArtifactPath != null
        }
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}