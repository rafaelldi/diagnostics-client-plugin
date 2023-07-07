package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.RecentArtifactList
import com.github.rafaelldi.diagnosticsclientplugin.topics.ArtifactListener
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import com.jetbrains.rd.platform.util.lifetime
import java.nio.file.Path
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class RecentArtifactTab(project: Project) :
    SimpleToolWindowPanel(false), ListSelectionListener {
    companion object {
        val RECENT_ARTIFACT_TAB: DataKey<RecentArtifactTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.RecentArtifactTab")
    }

    private val recentArtifactList = RecentArtifactList()

    var selectedArtifactPath: Path? = null
        private set

    init {
        val listPanel = JBScrollPane(recentArtifactList)
        listPanel.border = JBUI.Borders.empty()

        setContent(listPanel)
        initActionToolbar()

        project.lifetime.bracketIfAlive(
            { recentArtifactList.addListSelectionListener(this) },
            { recentArtifactList.removeListSelectionListener(this) }
        )

        project.messageBus.connect().subscribe(ArtifactListener.TOPIC,
            object : ArtifactListener {
                override fun artifactCreated(path: String) {
                    recentArtifactList.add(Path.of(path))
                }
            }
        )
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.RecentArtifacts") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.RecentArtifacts.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = recentArtifactList
        toolbar = actionToolbar.component
    }

    override fun valueChanged(e: ListSelectionEvent?) {
        if (e == null) {
            return
        }

        selectedArtifactPath = recentArtifactList.selectedArtifactPath
    }

    override fun getData(dataId: String): Any? {
        if (RECENT_ARTIFACT_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    fun removeArtifact(artifactPath: Path) {
        recentArtifactList.remove(artifactPath)
    }
}