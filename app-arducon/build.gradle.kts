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

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.window.manager)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(libs.play.services.ad)
}




