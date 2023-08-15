package com.github.rafaelldi.diagnosticsclientplugin.settings

import com.intellij.openapi.components.BaseState

class DiagnosticsClientSettingsState : BaseState() {
    var showChartsOnStart by property(false)
}