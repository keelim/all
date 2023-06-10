plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.keelim.compose"
}

dependencies {
    implementation(project(":common"))

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.material)
    implementation(libs.material.themAdapter)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.compose.ui.test)
}
