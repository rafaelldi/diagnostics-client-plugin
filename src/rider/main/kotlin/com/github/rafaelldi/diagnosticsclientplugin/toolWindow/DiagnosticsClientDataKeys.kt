package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorCountersTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorGcTab
import com.intellij.openapi.actionSystem.DataKey

class DiagnosticsClientDataKeys {
    companion object {
        val MONITOR_COUNTERS_TAB: DataKey<MonitorCountersTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.MonitorCountersTab")
        val MONITOR_GC_TAB: DataKey<MonitorGcTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.MonitorGcTab")
    }
}