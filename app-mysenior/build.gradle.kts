plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    defaultConfig {
        applicationId = "com.keelim.mysenior"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildTypes {
            create("my-mysenior-benchmark") {
                signingConfig = signingConfigs.getByName("debug")
                matchingFallbacks += listOf("release")
                isDebuggable = false
            }
        }
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.mysenior"
}

dependencies {
    implementation(projects.core.composeCore)
    implementation(projects.features.uiSetting)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.window.manager)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)
    implementation(libs.deeplinkdispatch)
    implementation(libs.firebase.config)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    ksp(libs.deeplinkdispatch.processor)
}
