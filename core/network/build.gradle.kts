plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.keelim.core.network"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(libs.kotlinx.datetime)
    api(projects.core.common)
    api(projects.core.model)

    implementation(platform(libs.coil.bom))
    implementation(libs.coil)
    implementation(libs.coil.svg)

    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
    implementation(libs.timber)

    implementation(libs.okhttp.logging)

    implementation(libs.retrofit.core)
}
