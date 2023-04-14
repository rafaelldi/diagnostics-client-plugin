package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle

enum class SourceProcessType(val label: String) {
    Attach(DiagnosticsClientBundle.message("dialog.source.process.type.attach")),
    Launch(DiagnosticsClientBundle.message("dialog.source.process.type.launch"))
}