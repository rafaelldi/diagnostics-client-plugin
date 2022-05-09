package icons

import com.intellij.openapi.util.IconLoader

@Suppress("unused")
object DiagnosticsClientIcons {
    @JvmField
    val ToolWindow = IconLoader.getIcon("/icons/diagnosticsClientTool.svg", javaClass)
    @JvmField
    val Counters = IconLoader.getIcon("/icons/counters.svg", javaClass)
    @JvmField
    val Traces = IconLoader.getIcon("/icons/traces.svg", javaClass)
}