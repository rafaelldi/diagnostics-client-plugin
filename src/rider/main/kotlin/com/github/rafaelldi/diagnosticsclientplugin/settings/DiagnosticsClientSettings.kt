package com.github.rafaelldi.diagnosticsclientplugin.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@State(
    name = "com.github.rafaelldi.diagnosticsclientplugin.settings.DiagnosticsClientSettingsState",
    storages = [(Storage("DiagnosticsClientSettings.xml"))]
)
@Service
class DiagnosticsClientSettings :
    SimplePersistentStateComponent<DiagnosticsClientSettingsState>(DiagnosticsClientSettingsState()) {
    companion object {
        fun getInstance() = service<DiagnosticsClientSettings>()
    }

    var showChartsOnStart
        get() = state.showChartsOnStart
        set(value) {
            state.showChartsOnStart = value
        }
}