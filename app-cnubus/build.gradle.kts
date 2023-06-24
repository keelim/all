plugins {
    id("keelim.android.application")
    id("keelim.android.application.firebase")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
}

android {
    defaultConfig {
        applicationId = "com.keelim.cnubus"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }
    buildTypes {
        create("cnubus-benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    namespace = "com.keelim.cnubus"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose:compose-core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":features:ui-labs"))
    implementation(project(":features:ui-map"))
    implementation(project(":features:ui-setting"))

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.work.ktx)
    implementation(libs.coil.kt)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.ui.auth)
    implementation(libs.hilt.ext.work)
    implementation(libs.lottie)
    implementation(libs.play.services.ad)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    kapt(libs.hilt.ext.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test)
}
