@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.TriggerGcCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.withBackgroundProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchOnUi
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.solution

@Service
class TriggerGcCollectionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    fun triggerGc(pid: Int) {
        projectComponentLifetime.launchOnUi {
            withBackgroundProgressIndicator(project, "Triggering GC") {
                hostModel.triggerGc.startSuspending(TriggerGcCommand(pid))
            }
        }
    }
}