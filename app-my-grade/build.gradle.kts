plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.qodana")
}

android {
    defaultConfig {
        applicationId = "com.keelim.mygrade"
        buildTypes {
            create("app-my-grade-benchmark") {
                signingConfig = signingConfigs.getByName("debug")
                matchingFallbacks += listOf("release")
                isDebuggable = false
            }
        }
    }

    useLibrary("android.test.mock")
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.keelim.mygrade"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.material)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.lifecycle.process)

    implementation(libs.androidx.profileinstaller)

    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)

    implementation(libs.timber)
    implementation(libs.apache.math)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performances)
    implementation(libs.firebase.config)

    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.inapp.update)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.androidx.metrics)

    // modern storage
    implementation(libs.modernstorage.bom)
    implementation("com.google.modernstorage:modernstorage-permissions")
    implementation("com.google.modernstorage:modernstorage-storage")
    implementation("com.google.modernstorage:modernstorage-photopicker")
    implementation("com.squareup.okio:okio")

    implementation(libs.androidx.activity.compose)
}




