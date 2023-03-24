package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveTraceSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.ide.model.Solution
import com.jetbrains.rd.protocol.ProtocolExtListener
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class TraceTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): TraceTabManager = project.service()
    }

    private fun addTraceSessionTab(lt: Lifetime, pid: Int, session: LiveTraceSession) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
        val contentFactory = ContentFactory.getInstance()
        val liveTraceSessionTab = LiveTraceSessionTab(pid, session, this, project, lt)
        val content = contentFactory.createContent(liveTraceSessionTab, "Traces for $pid", true)
        content.icon = AllIcons.Toolwindows.ToolWindowMessages
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(lt) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.Toolwindows.ToolWindowMessages)
            val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.Toolwindows.ToolWindowMessages
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(TraceSessionListener.TOPIC).sessionClosed(pid)
    }

    fun activateTab(pid: Int) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
        val content = toolWindow.contentManager.findContent("Traces for $pid") ?: return
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }

    class ProtocolListener : ProtocolExtListener<Solution, DiagnosticsHostModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            project: Project,
            parent: Solution,
            model: DiagnosticsHostModel
        ) {
            model.liveTraceSessions.view(lifetime) { lt, pid, session ->
                getInstance(project).addTraceSessionTab(lt, pid, session)
            }
        }
    }
}