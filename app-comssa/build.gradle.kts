plugins {
    id("keelim.android.application")
    id("keelim.android.application.firebase")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
    id("androidx.navigation.safeargs.kotlin") version ("2.5.3")
}

android {
    defaultConfig {
        applicationId = "com.keelim.comssa"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        defaultConfig {}
        create("comssa-benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
        getByName("release") {}
    }
    namespace = "com.keelim.comssa"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    implementation(libs.play.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.timber)
    implementation(libs.play.services.ad)

    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.androidx.startup)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.runtime)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.androidx.paging.runtime)
}
