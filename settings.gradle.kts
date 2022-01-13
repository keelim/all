enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    val agpVersion = "7.0.4"
    val kotlinVersion = "1.5.31"

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version agpVersion
        id("com.android.library") version agpVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.parcelize") version kotlinVersion
//        id("com.google.devtools.ksp") version "${kotlinVersion}-1.0.0"
    }
    resolutionStrategy {
        eachPlugin {
            when (val id = requested.id.id) {
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
                "org.jetbrains.kotlin.plugin.serialization" -> useModule("${id}:${id}.gradle.plugin:${kotlinVersion}")
            }
        }
    }
}

rootProject.name = "nandaDiagnosis"
include(
    ":app",
    ":data",
    ":common",
    ":compose",
    ":domain",
    ":features:ui-category",
    ":features:player",
    ":features:ui-setting"
)

include(":features:dynamic_vitamin")
