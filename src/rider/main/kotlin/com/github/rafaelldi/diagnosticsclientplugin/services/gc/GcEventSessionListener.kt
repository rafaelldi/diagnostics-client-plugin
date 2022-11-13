package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.intellij.util.messages.Topic

interface GcEventSessionListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("GC Event Session Listener", GcEventSessionListener::class.java)
    }

    fun sessionClosed(pid: Int)
}