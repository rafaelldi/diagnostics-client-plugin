package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle

enum class DumpType(val label: String) {
    Full(DiagnosticsClientBundle.message("dialog.dump.type.full")),
    Heap(DiagnosticsClientBundle.message("dialog.dump.type.heap")),
    Triage(DiagnosticsClientBundle.message("dialog.dump.type.triage")),
    Mini(DiagnosticsClientBundle.message("dialog.dump.type.mini"))
}

fun DumpType.map(): com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType = when (this) {
    DumpType.Full -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Full
    DumpType.Heap -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Heap
    DumpType.Triage -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Triage
    DumpType.Mini -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Mini
}
