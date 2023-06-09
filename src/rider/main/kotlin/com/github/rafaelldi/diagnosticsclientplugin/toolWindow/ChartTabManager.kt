package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveChartSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.client.ProtocolProjectSession
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class ChartTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): ChartTabManager = project.service()
    }

    private fun addChartSessionTab(lt: Lifetime, pid: Int, session: LiveChartSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val contentFactory = ContentFactory.getInstance()
        val liveChartSessionTab = LiveChartSessionTab(pid, session, this, lt)
        val content = contentFactory.createContent(liveChartSessionTab, "Chart for $pid", true)
        content.icon = AllIcons.RunConfigurations.TestCustom
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(lt) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.RunConfigurations.TestCustom)
            val toolWindow =
                ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.RunConfigurations.TestCustom
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(ChartSessionListener.TOPIC).sessionClosed(pid)
    }

    class ProtocolListener : SolutionExtListener<DiagnosticsHostModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            session: ProtocolProjectSession,
            model: DiagnosticsHostModel
        ) {
            model.liveChartSessions.view(lifetime) { lt, pid, s ->
                getInstance(session.project).addChartSessionTab(lt, pid, s)
            }
        }
    }
}