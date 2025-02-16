import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
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
}

android {
    namespace = "com.keelim.core.resource"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
