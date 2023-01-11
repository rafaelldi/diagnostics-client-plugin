package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessInfo
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.RevealFileAction
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.ui.dsl.builder.BottomGap
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import icons.RiderIcons
import java.awt.datatransfer.StringSelection
import kotlin.io.path.Path

class ProcessDashboardPanel(
    pid: Int,
    processInfo: ProcessInfo
) : BorderLayoutPanel() {

    init {
        border = JBUI.Borders.empty(5, 10)

        val panel = panel {
            row {
                icon(RiderIcons.RunConfigurations.DotNetProject)
                    .gap(RightGap.SMALL)
                copyableLabel(processInfo.processName)
                    .bold()
                    .gap(RightGap.SMALL)
                copyableLabel(pid.toString())
            }
            separator()
            row {
                label("Properties").bold()
            }.bottomGap(BottomGap.SMALL)
            row {
                val filename = processInfo.filename ?: ""
                copyableLabel("Filename = $filename")
                inlineIconButton(AllIcons.General.InlineCopyHover, AllIcons.General.InlineCopy, filename) {
                    CopyPasteManager.getInstance().setContents(StringSelection(it))
                }
                    .gap(RightGap.SMALL)
                if (processInfo.filename.isNullOrEmpty().not()) {
                    inlineIconButton(AllIcons.General.OpenDisk, AllIcons.General.OpenDiskHover, filename) {
                        RevealFileAction.openFile(Path(it))
                    }
                }
            }
            row {
                val startTime = processInfo.startTime ?: ""
                copyableLabel("Start Time = $startTime")
                if (processInfo.startTime.isNullOrEmpty().not()) {
                    inlineIconButton(AllIcons.General.InlineCopyHover, AllIcons.General.InlineCopy, startTime) {
                        CopyPasteManager.getInstance().setContents(StringSelection(it))
                    }
                }
            }
            row {
                val commandLine = processInfo.commandLine ?: ""
                copyableLabel("Command Line = $commandLine")
                if (processInfo.commandLine.isNullOrEmpty().not()) {
                    inlineIconButton(AllIcons.General.InlineCopyHover, AllIcons.General.InlineCopy, commandLine) {
                        CopyPasteManager.getInstance().setContents(StringSelection(it))
                    }
                }
            }
            row {
                val operatingSystem = processInfo.operatingSystem ?: ""
                copyableLabel("Operating System = $operatingSystem")
                if (processInfo.operatingSystem.isNullOrEmpty().not()) {
                    inlineIconButton(AllIcons.General.InlineCopyHover, AllIcons.General.InlineCopy, operatingSystem) {
                        CopyPasteManager.getInstance().setContents(StringSelection(it))
                    }
                }
            }
            row {
                val architecture = processInfo.processArchitecture ?: ""
                copyableLabel("Process Architecture = $architecture")
                if (processInfo.processArchitecture.isNullOrEmpty().not()) {
                    inlineIconButton(AllIcons.General.InlineCopyHover, AllIcons.General.InlineCopy, architecture) {
                        CopyPasteManager.getInstance().setContents(StringSelection(it))
                    }
                }
            }
            separator()
            if (processInfo.environmentVariables.isNotEmpty()) {
                row {
                    label("Environment variables").bold()
                }.bottomGap(BottomGap.SMALL)
                for (envVar in processInfo.environmentVariables) {
                    row {
                        copyableLabel("${envVar.key} = ${envVar.value}")
                    }
                }
            }
        }

        add(panel)
    }
}