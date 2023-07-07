package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.model.ProcessInfo
import com.intellij.ide.CopyProvider
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.treeStructure.SimpleTree
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.tree.TreeUtil
import java.awt.datatransfer.StringSelection
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

        TreeSpeedSearch.installOn(this, true) {
            return@installOn when (val node = it.lastPathComponent) {
                is ProcessTreeNode -> node.name
                else -> null
            }
        }

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