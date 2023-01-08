package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution

@Service
class GcEventTabManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): GcEventTabManager = project.service()
    }

    init {
        val model = project.solution.diagnosticsHostModel
        model.liveGcEventSessions.view(projectComponentLifetime) { lt, pid, session ->
            addGcEventSessionTab(lt, pid, session)
        }
    }

    private fun addGcEventSessionTab(lt: Lifetime, pid: Int, session: LiveGcEventSession) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
        val contentFactory = ContentFactory.getInstance()
        val gcEventMonitoringTab = GcEventMonitoringTab(pid, session, this, lt)
        val content = contentFactory.createContent(gcEventMonitoringTab, "GC for $pid", true)
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
            val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.Actions.GC
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(GcEventSessionListener.TOPIC).sessionClosed(pid)
    }
}