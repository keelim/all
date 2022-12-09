plugins {
    id("keelim.android.application")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("androidx.navigation.safeargs.kotlin") version ("2.4.2")
}

android {
    defaultConfig {
        applicationId = "com.keelim.comssa"
        versionCode = 10
        versionName = "1.0.10"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        defaultConfig {}
        getByName("release") {}
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.3.2" }
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

    implementation("com.google.android.play:core-ktx:1.8.1")

    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation(libs.timber)
    implementation(libs.play.services.ad)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

    implementation(libs.androidx.startup)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)

    // compose
    val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
    implementation("androidx.compose.ui:ui")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.activity.compose)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.runtime:runtime-livedata")

    implementation(libs.androidx.paging.runtime)
}
