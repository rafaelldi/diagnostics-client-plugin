package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.ProcessInfo
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessDashboardPanel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessTreeComponent
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostListener
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostProcessListener
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import com.jetbrains.rd.platform.util.lifetime
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener

class ProcessExplorerTab(project: Project) : SimpleToolWindowPanel(false), TreeSelectionListener {
    companion object {
        const val SPLITTER_PROPORTION = 0.3F
        val PROCESS_TREE: DataKey<ProcessTreeComponent> = DataKey.create("DiagnosticsClient.ProcessTree")
    }

    private val splitter: OnePixelSplitter
    private val processTree: ProcessTreeComponent = ProcessTreeComponent()

    init {
        splitter = OnePixelSplitter(false, SPLITTER_PROPORTION).apply {
            firstComponent = ScrollPaneFactory.createScrollPane(processTree, SideBorder.NONE)
            secondComponent = JBPanelWithEmptyText()
        }

        setContent(splitter)
        initActionToolbar()

        project.lifetime.bracketIfAlive(
            { processTree.addTreeSelectionListener(this) },
            { processTree.removeTreeSelectionListener(this) }
        )

        project.messageBus.connect().subscribe(HostProcessListener.TOPIC,
            object : HostProcessListener {
                override fun processAdded(pid: Int, processInfo: ProcessInfo) {
                    processTree.addProcessNode(pid, processInfo)
                }

                override fun processRemoved(pid: Int, processInfo: ProcessInfo) {
                    processTree.removeProcessNode(pid)
                }
            }
        )

        project.messageBus.connect().subscribe(HostListener.TOPIC,
            object : HostListener {
                override fun hostDisconnected() {
                    processTree.clear()
                }
            })
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.Explorer") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.Explorer.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    override fun valueChanged(e: TreeSelectionEvent?) {
        if (e == null) {
            return
        }

        val localProcessNode = processTree.selectedNode as? LocalProcessNode
        if (localProcessNode != null) {
            val panel = ProcessDashboardPanel(localProcessNode.processId, localProcessNode.processInfo)
            val processDashboardPanel = JBScrollPane(panel)
            processDashboardPanel.border = JBUI.Borders.empty()
            splitter.secondComponent = processDashboardPanel
        } else {
            splitter.secondComponent = JBPanelWithEmptyText()
        }
    }

    override fun getData(dataId: String): Any? {
        if (PROCESS_TREE.`is`(dataId)) return processTree
        return super.getData(dataId)
    }
}