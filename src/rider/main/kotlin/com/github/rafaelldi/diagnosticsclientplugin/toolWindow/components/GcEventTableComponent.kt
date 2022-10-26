package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEvent
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class GcEventTableComponent : JBTable() {
    companion object {
        private const val NUMBER_COLUMN_TITLE = "Number"
        private const val GENERATION_COLUMN_TITLE = "Gen"
        private const val REASON_COLUMN_TITLE = "Trigger Reason"
        private const val PAUSE_COLUMN_TITLE = "Pause MSec"
        private const val PEAK_COLUMN_TITLE = "Peak MB"
        private const val AFTER_COLUMN_TITLE = "After MB"
        private const val RATIO_COLUMN_TITLE = "Ratio"
        private const val PROMOTED_COLUMN_TITLE = "Promoted MB"
        private const val ALLOCATED_COLUMN_TITLE = "Allocated MB"
        private const val ALLOCATION_RATE_COLUMN_TITLE = "Allocation Rate"
        private const val GEN0_COLUMN_TITLE = "Gen0 MB"
        private const val GEN1_COLUMN_TITLE = "Gen1 MB"
        private const val GEN2_COLUMN_TITLE = "Gen2 MB"
        private const val LOH_COLUMN_TITLE = "LOH MB"
        private const val PINNED_OBJECTS_COLUMN_TITLE = "Pinned Objects"
    }

    private val tableModel: DefaultTableModel =
        DefaultTableModel(
            arrayOf(
                NUMBER_COLUMN_TITLE,
                GENERATION_COLUMN_TITLE,
                REASON_COLUMN_TITLE,
                PAUSE_COLUMN_TITLE,
                PEAK_COLUMN_TITLE,
                AFTER_COLUMN_TITLE,
                RATIO_COLUMN_TITLE,
                PROMOTED_COLUMN_TITLE,
                ALLOCATED_COLUMN_TITLE,
                ALLOCATION_RATE_COLUMN_TITLE,
                GEN0_COLUMN_TITLE,
                GEN1_COLUMN_TITLE,
                GEN2_COLUMN_TITLE,
                LOH_COLUMN_TITLE,
                PINNED_OBJECTS_COLUMN_TITLE
            ), 0
        )

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun add(gcEvent: GcEvent) {
        tableModel.addRow(
            arrayOf(
                gcEvent.number,
                gcEvent.generation,
                gcEvent.reason,
                gcEvent.pauseDuration,
                gcEvent.peak,
                gcEvent.after,
                gcEvent.ratio,
                gcEvent.promoted,
                gcEvent.allocated,
                gcEvent.allocationRate,
                gcEvent.sizeGen0,
                gcEvent.sizeGen1,
                gcEvent.sizeGen2,
                gcEvent.sizeLoh,
                gcEvent.pinnedObjects
            )
        )
    }
}