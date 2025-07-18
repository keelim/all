plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.mygrade"
        buildTypes {
            // create("my-grade-benchmark") {
            //     signingConfig = signingConfigs.getByName("debug")
            //     matchingFallbacks += listOf("release")
            //     isDebuggable = false
            // }
        }
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.mygrade"
}

dependencies {

    implementation(projects.core.commonAndroid)

    implementation(projects.core.data)
    implementation(projects.core.navigation)

    implementation(projects.feature.uiSetting)
    implementation(projects.widget)

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.apache.math)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.play.services.ad)
    implementation(libs.timber)
    implementation(libs.deeplinkdispatch)
    implementation(libs.firebase.common)
    implementation(platform(libs.firebase.bom))
    ksp(libs.deeplinkdispatch.processor)

    implementation(libs.play.services.oss)
}




