package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(name = "GcSettings", storages = [(Storage("diagnostics-client.xml"))])
class GcSettings(project: Project) : SimplePersistentStateComponent<GcSettingsState>(GcSettingsState()) {
    companion object {
        @JvmStatic
        fun getInstance(project: Project): GcSettings = project.service()
    }

    fun getModel() = MonitorGcModel(state.stoppingType, state.duration)

    fun update(model: MonitorGcModel) {
        state.apply {
            stoppingType = model.stoppingType
            duration = model.duration
        }
    }
}

class GcSettingsState : BaseState() {
    var stoppingType by enum(StoppingType.AfterPeriod)
    var duration by property(30)
}