package project.side.ikdaman.app

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "project.side.ikdaman.app.$name"
    }
}