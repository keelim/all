plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
    kotlin("plugin.serialization")
}

kotlin {
    // web
    // js(IR) {
    //     browser()
    // }
    // android
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }

    // apple do not need x64
    // iosX64()
    iosArm64()
    iosSimulatorArm64()

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
