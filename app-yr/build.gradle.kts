plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    kotlin("kapt")
    // id("com.google.gms.google-services")
    // id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    defaultConfig {
        applicationId = "com.keelim.yr"
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.keelim.yr"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.work.ktx)
}
