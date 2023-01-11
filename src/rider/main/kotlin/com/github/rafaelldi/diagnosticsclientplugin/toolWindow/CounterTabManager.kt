package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import icons.DiagnosticsClientIcons

@Service
class CounterTabManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): CounterTabManager = project.service()
    }

    init {
        val model = project.solution.diagnosticsHostModel
        model.liveCounterSessions.view(projectComponentLifetime) { lt, pid, session ->
            addCounterSessionTab(lt, pid, session)
        }
    }

    private fun addCounterSessionTab(lt: Lifetime, pid: Int, session: LiveCounterSession) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
        val contentFactory = ContentFactory.getInstance()
        val liveCounterSessionTab = LiveCounterSessionTab(pid, session, this, lt)
        val content = contentFactory.createContent(liveCounterSessionTab, "Counters for $pid", true)
        content.icon = DiagnosticsClientIcons.Counters
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(lt) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(DiagnosticsClientIcons.Counters)
            val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = DiagnosticsClientIcons.Counters
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(CounterSessionListener.TOPIC).sessionClosed(pid)
    }
}