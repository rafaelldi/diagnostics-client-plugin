package com.github.rafaelldi.diagnosticsclientplugin.activity

import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.settings.DiagnosticsClientSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchBackground
import com.intellij.openapi.rd.util.lifetime
import com.intellij.openapi.startup.ProjectActivity

class ConnectToAgentActivity: ProjectActivity {
    override suspend fun execute(project: Project) {
        val settings = DiagnosticsClientSettings.getInstance()
        if (!settings.connectToAgent) return

        val host = DiagnosticsHost.getInstance(project)
        if (host.isConnected) return

        project.lifetime.launchBackground {
            host.connectToAgent(true)
        }
    }
}