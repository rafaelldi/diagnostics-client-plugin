package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.ide.model.Solution
import com.jetbrains.rd.protocol.ProtocolExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import icons.DiagnosticsClientIcons

@Service
class CounterTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): CounterTabManager = project.service()
    }

    private fun addCounterSessionTab(lt: Lifetime, pid: Int, session: LiveCounterSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
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
            val toolWindow =
                ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = DiagnosticsClientIcons.Counters
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(CounterSessionListener.TOPIC).sessionClosed(pid)
    }

    fun activateTab(pid: Int) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val content = toolWindow.contentManager.findContent("Counters for $pid") ?: return
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }

    class ProtocolListener : ProtocolExtListener<Solution, DiagnosticsHostModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            project: Project,
            parent: Solution,
            model: DiagnosticsHostModel
        ) {
            model.liveCounterSessions.view(lifetime) { lt, pid, session ->
                getInstance(project).addCounterSessionTab(lt, pid, session)
            }
        }
    }
}