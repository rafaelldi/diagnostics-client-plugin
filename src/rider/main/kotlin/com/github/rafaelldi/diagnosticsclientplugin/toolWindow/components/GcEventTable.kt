package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEvent
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class GcEventTable : JBTable() {
    companion object {
        private val NUMBER_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.number.column")
        private val GENERATION_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.gen.column")
        private val REASON_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.trigger.reason.column")
        private val PAUSE_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.pause.column")
        private val PEAK_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.peak.column")
        private val AFTER_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.after.column")
        private val RATIO_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.ratio.column")
        private val PROMOTED_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.promoted.column")
        private val ALLOCATED_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.allocated.column")
        private val ALLOCATION_RATE_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.allocation.rate.column")
        private val GEN0_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.gen.0.column")
        private val GEN1_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.gen.1.column")
        private val GEN2_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.gen.2.column")
        private val LOH_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.loh.column")
        private val PINNED_OBJECTS_COLUMN_TITLE = DiagnosticsClientBundle.message("gc.table.pinned.objects.column")
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