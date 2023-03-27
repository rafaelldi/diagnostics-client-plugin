package com.github.rafaelldi.diagnosticsclientplugin.utils

import kotlinx.datetime.LocalDateTime
import java.nio.file.Path

data class DiagnosticsArtifact(val path: Path, val creationDate: LocalDateTime)
