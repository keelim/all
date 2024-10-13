plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.cnubus"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }
    buildTypes {
        // create("cnubus-benchmark") {
        //     signingConfig = signingConfigs.getByName("debug")
        //     matchingFallbacks += listOf("release")
        //     isDebuggable = false
        // }
    }
    namespace = "com.keelim.cnubus"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.commonAndroid)
    implementation(projects.core.composeCore)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.shared)
    implementation(projects.core.domain)
    implementation(projects.feature.uiLabs)
    implementation(projects.feature.uiSetting)
    implementation(projects.widget)
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widget)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.deeplinkdispatch)
    ksp(libs.deeplinkdispatch.processor)
}
