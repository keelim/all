plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    id("keelim.android.hilt")
    id("kotlinx-serialization")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    // coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    // compose
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.glance)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material.themAdapter)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.hilt.navigation.compose)
}
android {
    namespace = "com.keelim.compose"
}
