package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle

enum class TracingProfile(private val label: String) {
    None(DiagnosticsClientBundle.message("dialog.tracing.profile.none")),
    CpuSampling(DiagnosticsClientBundle.message("dialog.tracing.profile.cpu.sampling")),
    GcVerbose(DiagnosticsClientBundle.message("dialog.tracing.profile.gc.verbose")),
    GcCollect(DiagnosticsClientBundle.message("dialog.tracing.profile.gc.collect"));

    override fun toString() = label
}

fun TracingProfile.map(): com.github.rafaelldi.diagnosticsclientplugin.model.TracingProfile = when (this) {
    TracingProfile.None -> com.github.rafaelldi.diagnosticsclientplugin.model.TracingProfile.None
    TracingProfile.CpuSampling -> com.github.rafaelldi.diagnosticsclientplugin.model.TracingProfile.CpuSampling
    TracingProfile.GcVerbose -> com.github.rafaelldi.diagnosticsclientplugin.model.TracingProfile.GcVerbose
    TracingProfile.GcCollect -> com.github.rafaelldi.diagnosticsclientplugin.model.TracingProfile.GcCollect
}