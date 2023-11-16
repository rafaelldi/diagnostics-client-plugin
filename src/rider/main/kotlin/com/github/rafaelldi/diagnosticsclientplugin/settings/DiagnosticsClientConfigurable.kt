package com.github.rafaelldi.diagnosticsclientplugin.settings

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

class DiagnosticsClientConfigurable :
    BoundConfigurable(DiagnosticsClientBundle.message("configurable.DiagnosticsClient")) {
    private val settings get() = DiagnosticsClientSettings.getInstance()

    override fun createPanel() = panel {
        row {
            checkBox(DiagnosticsClientBundle.message("configurable.DiagnosticsClient.connect.to.agent"))
                .bindSelected(settings::connectToAgent)
        }
        row {
            checkBox(DiagnosticsClientBundle.message("configurable.DiagnosticsClient.show.charts"))
                .bindSelected(settings::showChartsOnStart)
        }
    }
}