package com.github.rafaelldi.diagnosticsclientplugin.services

import com.intellij.openapi.project.Project
import com.github.rafaelldi.diagnosticsclientplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
