package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.InstallGlobalToolAction
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.UpdateGlobalToolAction
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.util.ExecUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.EnvironmentUtil
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost

@Service
class DiagnosticsToolService(private val project: Project) {
    companion object {
        private const val WINDOWS_HOME_VARIABLE = "USERPROFILE"
        private const val LINUX_HOME_VARIABLE = "HOME"
        private const val CURRENT_VERSION = "2023.2.0-preview3"

        private val LOG = logger<DiagnosticsToolService>()

        fun getDefaultGlobalPath(): String {
            val homeFolder =
                if (SystemInfo.isWindows) EnvironmentUtil.getValue(WINDOWS_HOME_VARIABLE)
                else EnvironmentUtil.getValue(LINUX_HOME_VARIABLE)

            return if (SystemInfo.isWindows)
                "$homeFolder\\.dotnet\\tools\\dotnet-diagnostics-agent.exe"
            else
                "$homeFolder/.dotnet/tools/dotnet-diagnostics-agent"
        }

        fun getInstance(project: Project) = project.service<DiagnosticsToolService>()
    }

    fun checkGlobalTool(): Boolean {
        val dotnetPath = getDotnetPath() ?: return false
        val isInstalled = isGlobalToolInstalled(dotnetPath)
        if (!isInstalled) {
            Notification(
                "Diagnostics Client",
                DiagnosticsClientBundle.message("notification.global.tool.agent.not.installed"),
                DiagnosticsClientBundle.message("notification.global.tool.please.install.agent"),
                NotificationType.WARNING
            )
                .addAction(InstallGlobalToolAction())
                .notify(project)
            return false
        }

        val isCurrentVersionInstalled = isCurrentVersionInstalled(dotnetPath)
        if (!isCurrentVersionInstalled) {
            Notification(
                "Diagnostics Client",
                DiagnosticsClientBundle.message("notification.global.tool.new.version.available"),
                DiagnosticsClientBundle.message("notification.global.tool.please.update.agent"),
                NotificationType.WARNING
            )
                .addAction(UpdateGlobalToolAction())
                .notify(project)
            return false
        }

        return true
    }

    fun run(port: Int): KillableColoredProcessHandler {
        val toolPath = getDefaultGlobalPath()
        val arguments = listOf("run", "-p", port.toString())
        val cmd = GeneralCommandLine()
            .withExePath(toolPath)
            .withParameters(arguments)

        return KillableColoredProcessHandler.Silent(cmd)
    }

    suspend fun installGlobalTool() {
        val dotnetPath = getDotnetPath() ?: return

        val commandLine = GeneralCommandLine()
            .withExePath(dotnetPath)
            .withParameters("tool", "install", "Rafaelldi.DiagnosticsAgent", "--global", "--version", CURRENT_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.checkSuccess(LOG)) {
            withUiContext {
                Notification(
                    "Diagnostics Client",
                    DiagnosticsClientBundle.message("notification.global.tool.installed"),
                    "",
                    NotificationType.INFORMATION
                )
                    .notify(project)
            }
        } else {
            withUiContext {
                Notification(
                    "Diagnostics Client",
                    DiagnosticsClientBundle.message("notification.global.tool.installation.failed"),
                    output.stderr,
                    NotificationType.ERROR
                )
                    .notify(project)
            }
        }
    }

    suspend fun updateGlobalTool() {
        val dotnetPath = getDotnetPath() ?: return

        val commandLine = GeneralCommandLine()
            .withExePath(dotnetPath)
            .withParameters("tool", "update", "Rafaelldi.DiagnosticsAgent", "--global", "--version", CURRENT_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.checkSuccess(LOG)) {
            withUiContext {
                Notification(
                    "Diagnostics Client",
                    DiagnosticsClientBundle.message("notification.global.tool.updated"),
                    "",
                    NotificationType.INFORMATION
                )
                    .notify(project)
            }
        } else {
            withUiContext {
                Notification(
                    "Diagnostics Client",
                    DiagnosticsClientBundle.message("notification.global.tool.update.failed"),
                    output.stderr,
                    NotificationType.ERROR
                )
                    .notify(project)
            }
        }
    }

    private fun getDotnetPath(): String? {
        return RiderDotNetActiveRuntimeHost.getInstance(project).dotNetCoreRuntime.value?.cliExePath
    }

    private fun getListOfGlobalTools(dotnetPath: String): ProcessOutput {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(dotnetPath)
            .withParameters("tool", "list", "--global")

        return ExecUtil.execAndGetOutput(commandLine)
    }

    private fun isGlobalToolInstalled(dotnetPath: String): Boolean {
        val output = getListOfGlobalTools(dotnetPath)

        return if (output.checkSuccess(LOG)) {
            val regex = Regex("^rafaelldi\\.diagnosticsagent", RegexOption.MULTILINE)
            regex.containsMatchIn(output.stdout)
        } else {
            false
        }
    }

    private fun isCurrentVersionInstalled(dotnetPath: String): Boolean {
        val version = getGlobalToolVersion(dotnetPath) ?: return false
        return version == CURRENT_VERSION
    }

    private fun getGlobalToolVersion(dotnetPath: String): String? {
        val output = getListOfGlobalTools(dotnetPath)

        return if (output.checkSuccess(LOG)) {
            val regex = Regex("^rafaelldi\\.diagnosticsagent\\s+([\\w.\\-]+)", RegexOption.MULTILINE)
            regex.find(output.stdout)?.groups?.get(1)?.value ?: return null
        } else {
            null
        }
    }
}