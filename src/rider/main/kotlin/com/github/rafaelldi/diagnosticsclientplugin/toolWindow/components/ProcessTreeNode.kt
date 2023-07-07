package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.model.ProcessInfo
import com.intellij.icons.AllIcons
import com.intellij.ui.ColoredTreeCellRenderer
import icons.RiderIcons
import javax.swing.tree.DefaultMutableTreeNode

sealed class ProcessTreeNode(val name: String) : DefaultMutableTreeNode() {
    abstract fun render(renderer: ColoredTreeCellRenderer)
}

class LocalRootNode : ProcessTreeNode("Local") {
    override fun render(renderer: ColoredTreeCellRenderer) {
        renderer.append(name)
        renderer.icon = AllIcons.Nodes.HomeFolder
    }
}

class LocalProcessNode(val processId: Int, val processInfo: ProcessInfo) : ProcessTreeNode(processInfo.processName) {
    override fun render(renderer: ColoredTreeCellRenderer) {
        renderer.append("$processId - $name")
        renderer.icon = RiderIcons.RunConfigurations.DotNetProject
    }
}
