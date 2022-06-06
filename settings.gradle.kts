pluginManagement {
    includeBuild("build-logic")
    val agpVersion = "7.2.0"
    val kotlinVersion = "1.6.10"
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version agpVersion
        id("com.android.library") version agpVersion
        id("org.jetbrains.kotlin.android") version "1.6.10"
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.parcelize") version kotlinVersion
        id("com.google.devtools.ksp") version "$kotlinVersion-1.0.4"
    }
    resolutionStrategy {
        eachPlugin {
            when (val id = requested.id.id) {
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:2.42")
                "org.jetbrains.kotlin.plugin.serialization" -> useModule("${id}:${id}.gradle.plugin:${kotlinVersion}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "MyGrade"
include(
    ":app",
    ":compose",
    ":data",
    ":common",
    "benchmark"
)
