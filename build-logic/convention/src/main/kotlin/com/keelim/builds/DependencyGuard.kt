package com.keelim.builds

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureDependencyGuard() {
    configure<DependencyGuardPluginExtension> {
        configuration("releaseRuntimeClasspath")
    }
}
