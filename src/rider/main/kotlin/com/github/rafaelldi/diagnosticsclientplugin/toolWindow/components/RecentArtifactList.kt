package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.utils.DiagnosticsArtifact
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.components.JBList
import java.nio.file.Path
import javax.swing.JList
import javax.swing.ListSelectionModel
import kotlin.io.path.pathString

class RecentArtifactList : JBList<DiagnosticsArtifact>() {
    private val listModel = createDefaultListModel<DiagnosticsArtifact>()

    init {
        model = listModel
        emptyText.text = "No recent artifacts"
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = CellRenderer()
    }

    val selectedArtifactPath: Path?
        get() = selectedValue?.path

    fun add(artifact: DiagnosticsArtifact) {
        if (!listModel.elements().asSequence().map { it.path }.contains(artifact.path)) {
            listModel.addElement(artifact)
        }
    }

    fun remove(artifact: DiagnosticsArtifact) {
        listModel.removeElement(artifact)
    }

    private class CellRenderer : ColoredListCellRenderer<DiagnosticsArtifact>() {
        override fun customizeCellRenderer(
            list: JList<out DiagnosticsArtifact>,
            value: DiagnosticsArtifact?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            if (value == null) {
                return
            }

            append(value.path.pathString)
        }
    }
}