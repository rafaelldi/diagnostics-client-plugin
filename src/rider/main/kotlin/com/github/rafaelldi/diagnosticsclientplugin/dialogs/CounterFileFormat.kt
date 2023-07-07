package com.github.rafaelldi.diagnosticsclientplugin.dialogs

enum class CounterFileFormat {
    Csv,
    Json
}

fun CounterFileFormat.map(): com.github.rafaelldi.diagnosticsclientplugin.model.CounterFileFormat = when (this) {
    CounterFileFormat.Csv -> com.github.rafaelldi.diagnosticsclientplugin.model.CounterFileFormat.Csv
    CounterFileFormat.Json -> com.github.rafaelldi.diagnosticsclientplugin.model.CounterFileFormat.Json
}