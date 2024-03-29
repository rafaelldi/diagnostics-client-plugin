package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*

@Service
@State(name = "ChartSettings", storages = [(Storage("diagnostics-client.xml"))])
class ChartSettings : SimplePersistentStateComponent<ChartSettings.ChartSettingsState>(ChartSettingsState()) {
    companion object {
        fun getInstance(): ChartSettings = service()
    }

    fun getModel(selected: DotNetProcess) = ChartSessionModel(
        selected,
        state.stoppingType,
        state.duration
    )

    fun update(model: ChartSessionModel) {
        state.apply {
            stoppingType = model.stoppingType
            duration = model.duration
        }
    }

    class ChartSettingsState : BaseState() {
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
    }
}