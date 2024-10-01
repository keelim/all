plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.parcelize")
    kotlin("kapt")
}

android {
    defaultConfig {
        applicationId = "com.keelim.arducon"
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.arducon"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.composeCore)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.widget)

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.window.manager)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.jsoup)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.code.scanner)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
}




