package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveChartSessionTab
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
class ChartTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): ChartTabManager = project.service()
    }

    private fun addChartSessionTab(lt: Lifetime, pid: Int, session: LiveChartSession) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
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
            val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.RunConfigurations.TestCustom
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(ChartSessionListener.TOPIC).sessionClosed(pid)
    }

    class ProtocolListener : ProtocolExtListener<Solution, DiagnosticsHostModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            project: Project,
            parent: Solution,
            model: DiagnosticsHostModel
        ) {
            model.liveChartSessions.view(lifetime) { lt, pid, session ->
                getInstance(project).addChartSessionTab(lt, pid, session)
            }
        }
    }
}