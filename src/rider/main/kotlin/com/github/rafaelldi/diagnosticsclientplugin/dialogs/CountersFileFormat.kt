package com.github.rafaelldi.diagnosticsclientplugin.dialogs

enum class CountersFileFormat {
    Csv,
    Json
}

fun CountersFileFormat.map(): com.github.rafaelldi.diagnosticsclientplugin.generated.CountersFileFormat = when (this) {
    CountersFileFormat.Csv -> com.github.rafaelldi.diagnosticsclientplugin.generated.CountersFileFormat.Csv
    CountersFileFormat.Json -> com.github.rafaelldi.diagnosticsclientplugin.generated.CountersFileFormat.Json
}