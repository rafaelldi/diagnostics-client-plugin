package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.components.JBList
import java.nio.file.Path
import javax.swing.JList
import javax.swing.ListSelectionModel
import kotlin.io.path.pathString

class RecentArtifactList : JBList<Path>() {
    private val listModel = createDefaultListModel<Path>()

    init {
        model = listModel
        emptyText.text = DiagnosticsClientBundle.message("artifact.list.empty")
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = CellRenderer()
    }

    val selectedArtifactPath: Path?
        get() = selectedValue

    fun add(artifactPath: Path) {
        if (!listModel.elements().asSequence().contains(artifactPath)) {
            listModel.addElement(artifactPath)
        }
    }

    fun remove(artifactPath: Path) {
        listModel.removeElement(artifactPath)
    }

    private class CellRenderer : ColoredListCellRenderer<Path>() {
        override fun customizeCellRenderer(
            list: JList<out Path>,
            value: Path?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            if (value == null) {
                return
            }

            append(value.pathString)
        }
    }
}