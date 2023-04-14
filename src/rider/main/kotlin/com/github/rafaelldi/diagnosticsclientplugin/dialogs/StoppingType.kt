package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle

enum class StoppingType(val label: String) {
    AfterPeriod(DiagnosticsClientBundle.message("dialog.stopping.type.after.period")),
    Manually(DiagnosticsClientBundle.message("dialog.stopping.type.manually"))
}