package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.intellij.util.messages.Topic

interface ChartSessionListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("Chart Session Listener", ChartSessionListener::class.java)
    }

    fun sessionClosed(pid: Int)
}