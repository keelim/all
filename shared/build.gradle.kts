import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.keelim.android.application.room)
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ALL"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
            implementation(libs.circuit.foundation)

            api(projects.core.resource)
            api(libs.androidx.dataStore.preferences)
            api(libs.androidx.dataStore.core.okio)

            implementation(libs.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.kotlinx.datetime)
        }

        appleMain.dependencies {

        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

android {
    namespace = "com.keelim.kmp.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
