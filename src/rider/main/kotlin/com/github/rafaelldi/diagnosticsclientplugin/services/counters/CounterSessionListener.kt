package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.intellij.util.messages.Topic

interface CounterSessionListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("Counter Session Listener", CounterSessionListener::class.java)
    }

    fun sessionClosed(pid: Int)
}