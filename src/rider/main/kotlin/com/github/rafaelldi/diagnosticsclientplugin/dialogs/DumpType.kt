package com.github.rafaelldi.diagnosticsclientplugin.dialogs

enum class DumpType {
    Full,
    Heap,
    Triage,
    Mini
}

fun DumpType.map(): com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType = when (this) {
    DumpType.Full -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Full
    DumpType.Heap -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Heap
    DumpType.Triage -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Triage
    DumpType.Mini -> com.github.rafaelldi.diagnosticsclientplugin.generated.DumpType.Mini
}
