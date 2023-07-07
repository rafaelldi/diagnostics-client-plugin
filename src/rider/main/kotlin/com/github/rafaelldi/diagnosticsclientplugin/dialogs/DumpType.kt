package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle

enum class DumpType(val label: String) {
    Full(DiagnosticsClientBundle.message("dialog.dump.type.full")),
    Heap(DiagnosticsClientBundle.message("dialog.dump.type.heap")),
    Triage(DiagnosticsClientBundle.message("dialog.dump.type.triage")),
    Mini(DiagnosticsClientBundle.message("dialog.dump.type.mini"))
}

fun DumpType.map(): com.github.rafaelldi.diagnosticsclientplugin.model.DumpType = when (this) {
    DumpType.Full -> com.github.rafaelldi.diagnosticsclientplugin.model.DumpType.Full
    DumpType.Heap -> com.github.rafaelldi.diagnosticsclientplugin.model.DumpType.Heap
    DumpType.Triage -> com.github.rafaelldi.diagnosticsclientplugin.model.DumpType.Triage
    DumpType.Mini -> com.github.rafaelldi.diagnosticsclientplugin.model.DumpType.Mini
}
