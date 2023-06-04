plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("plugin.parcelize")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.keelim.setting"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.glance)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.material.themAdapter)
}
