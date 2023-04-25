package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
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

@Service
class GcEventTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): GcEventTabManager = project.service()
    }

    private fun addGcEventSessionTab(lt: Lifetime, pid: Int, session: LiveGcEventSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val contentFactory = ContentFactory.getInstance()
        val liveGcEventSessionTab = LiveGcEventSessionTab(pid, session, this, lt)
        val content = contentFactory.createContent(liveGcEventSessionTab, "GC for $pid", true)
        content.icon = AllIcons.Actions.GC
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(lt) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.Actions.GC)
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.Actions.GC
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(GcEventSessionListener.TOPIC).sessionClosed(pid)
    }

    fun activateTab(pid: Int) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val content = toolWindow.contentManager.findContent("GC for $pid") ?: return
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }

    class ProtocolListener : ProtocolExtListener<Solution, DiagnosticsHostModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            project: Project,
            parent: Solution,
            model: DiagnosticsHostModel
        ) {
            model.liveGcEventSessions.view(lifetime) { lt, pid, session ->
                getInstance(project).addGcEventSessionTab(lt, pid, session)
            }
        }
    }
}