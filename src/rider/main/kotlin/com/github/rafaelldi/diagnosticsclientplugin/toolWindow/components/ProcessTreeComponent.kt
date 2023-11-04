package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.model.ProcessInfo
import com.intellij.ide.CopyProvider
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.treeStructure.SimpleTree
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.tree.TreeUtil
import java.awt.datatransfer.StringSelection
import java.awt.event.InputEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath
import javax.swing.tree.TreeSelectionModel

class ProcessTreeComponent : SimpleTree(), CopyProvider {

    private val localRoot = LocalRootNode()
    private val modelLock = Any()

    init {
        val root = DefaultMutableTreeNode()
        root.add(localRoot)
        model = DefaultTreeModel(root)

        isRootVisible = false
        showsRootHandles = false
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        border = JBUI.Borders.emptyTop(5)

        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.Explorer.Popup") as ActionGroup
        setPopupGroup(actionGroup, ActionPlaces.TOOLWINDOW_POPUP)

        TreeSpeedSearch.installOn(this, true) {
            return@installOn when (val node = it.lastPathComponent) {
                is ProcessTreeNode -> node.name
                else -> null
            }
        }

        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                nodeSelected(event)
                return true
            }
        }.installOn(this)

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(event: KeyEvent) {
                val keyCode = event.keyCode
                if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                    nodeSelected(event)
                }
            }
        })

        cellRenderer = object : ColoredTreeCellRenderer() {
            override fun customizeCellRenderer(
                tree: JTree, value: Any?, selected: Boolean, expanded: Boolean,
                leaf: Boolean, row: Int, hasFocus: Boolean
            ) {
                if (value is ProcessTreeNode) {
                    value.render(this)
                }
            }
        }

        updateUI()
    }

    val selectedNode: ProcessTreeNode?
        get() = lastSelectedPathComponent as? ProcessTreeNode

    private fun nodeSelected(event: InputEvent) {
        val selected = selectedNode ?: return
        if (selected is LocalRootNode) {
            val action = ActionManager.getInstance().getAction("DiagnosticsClient.ToolWindow.Explorer.Connect")
            ActionUtil.invokeAction(action, this, ActionPlaces.TOOLWINDOW_CONTENT, event, null)
        } else if (selected is LocalProcessNode) {
            val action = ActionManager.getInstance().getAction("DiagnosticsClient.ToolWindow.Explorer.LiveChartSession.Start")
            ActionUtil.invokeAction(action, this, ActionPlaces.TOOLWINDOW_CONTENT, event, null)
        }
    }

    fun getLocalProcessNodes(): List<LocalProcessNode> = localRoot.children()
        .asSequence()
        .mapNotNull { it as? LocalProcessNode }
        .toList()

    fun addProcessNode(pid: Int, item: ProcessInfo) {
        synchronized(modelLock) {
            val node = LocalProcessNode(pid, item)

            val index = localRoot.children().asSequence().indexOfFirst {
                val current = it as? LocalProcessNode ?: return@indexOfFirst false
                node.processId < current.processId
            }

            (model as? DefaultTreeModel)
                ?.insertNodeInto(node, localRoot, if (index == -1) localRoot.childCount else index)

            expandPath(TreePath(localRoot.path))
        }
    }

    fun removeProcessNode(pid: Int) {
        synchronized(modelLock) {
            val node = TreeUtil.findNode(localRoot) {
                (it as? LocalProcessNode)?.processId == pid
            } ?: return

            (model as? DefaultTreeModel)?.removeNodeFromParent(node)
        }
    }

    fun clear() {
        synchronized(modelLock) {
            localRoot.removeAllChildren()
            (model as? DefaultTreeModel)?.reload(localRoot)
        }
    }

    override fun performCopy(dataContext: DataContext) {
        if (!isSelectionEmpty) {
            val selected = selectedNode ?: return
            CopyPasteManager.getInstance().setContents(StringSelection(selected.name))
        }
    }

    override fun isCopyEnabled(dataContext: DataContext) = !isSelectionEmpty

    override fun isCopyVisible(dataContext: DataContext) = !isSelectionEmpty

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}