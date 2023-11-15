package com.github.rafaelldi.diagnosticsclientplugin.settings

import com.intellij.openapi.components.BaseState

class DiagnosticsClientSettingsState : BaseState() {
    var connectToAgent by property(true)
    var showChartsOnStart by property(true)
}