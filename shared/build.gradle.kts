plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }
        commonMain.dependencies {
            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.paging)
            implementation(libs.sqldelight.primitive)
            implementation(libs.kotlinx.serialization.json)
            api(libs.androidx.dataStore.preferences)
            api(libs.androidx.dataStore.core.okio)
            implementation(libs.okio)
            implementation(libs.circuit.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
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
