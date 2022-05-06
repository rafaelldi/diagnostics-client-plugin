package com.github.rafaelldi.diagnosticsclientplugin.dialogs

enum class TracingProfile(val label: String) {
    None("none"),
    CpuSampling("cpu-sampling"),
    GcVerbose("gc-verbose"),
    GcCollect("gc-collect");

    override fun toString() = label
}

fun TracingProfile.map(): com.github.rafaelldi.diagnosticsclientplugin.generated.TracingProfile = when (this) {
    TracingProfile.None -> com.github.rafaelldi.diagnosticsclientplugin.generated.TracingProfile.None
    TracingProfile.CpuSampling -> com.github.rafaelldi.diagnosticsclientplugin.generated.TracingProfile.CpuSampling
    TracingProfile.GcVerbose -> com.github.rafaelldi.diagnosticsclientplugin.generated.TracingProfile.GcVerbose
    TracingProfile.GcCollect -> com.github.rafaelldi.diagnosticsclientplugin.generated.TracingProfile.GcCollect
}