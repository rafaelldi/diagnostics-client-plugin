package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.LiveCounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab.Companion.COUNTER_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class ResumeLiveCounterSessionAction : ResumeLiveSessionAction<LiveCounterSession, LiveCounterSessionTab>() {
    override val tabDatKey = COUNTER_MONITORING_TAB

    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            LiveCounterSessionController
                .getInstance(project)
                .startExistingSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.liveCounterSessions[pid]
}