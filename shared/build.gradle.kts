plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
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
            implementation(libs.sqldelight.android)
        }
        commonMain.dependencies {
            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.paging)
            implementation(libs.sqldelight.primitive)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
            implementation(libs.circuit.foundation)

            api(projects.core.resource)
            api(libs.androidx.dataStore.preferences)
            api(libs.androidx.dataStore.core.okio)
        }

        appleMain.dependencies {
            implementation(libs.sqldelight.native)
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName = "com.keelim.kmp.data"
        }
    }
}

android {
    namespace = "com.keelim.kmp.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
