package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.intellij.util.messages.Topic

interface TraceSessionListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("Trace Session Listener", TraceSessionListener::class.java)
    }

    fun sessionClosed(pid: Int)
}