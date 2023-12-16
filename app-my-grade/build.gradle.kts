plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
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
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:compose-core"))
    implementation(project(":core:data"))
    implementation(project(":features:ui-setting"))

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
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
    implementation(libs.apache.math)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)
    implementation(libs.inapp.update)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(libs.deeplinkdispatch)
    implementation(libs.firebase.common)
    implementation(platform(libs.firebase.bom))
    ksp(libs.deeplinkdispatch.processor)
}




