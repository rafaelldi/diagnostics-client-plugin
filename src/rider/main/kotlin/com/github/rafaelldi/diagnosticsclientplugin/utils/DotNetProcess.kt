package com.github.rafaelldi.diagnosticsclientplugin.utils

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode

data class DotNetProcess(val pid: Int, val name: String)

fun LocalProcessNode.toDotNetProcess() = DotNetProcess(this.processId, this.name)
