plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
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
    implementation(project(":compose:compose-core"))
    implementation(project(":features:ui-setting"))
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
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
