plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    id("keelim.android.hilt")
    kotlin("kapt")
}

android {
    namespace = "com.keelim.common"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.paging.common)
    implementation(libs.coil.kt)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.material)
    implementation(libs.material)
    implementation(libs.material.themAdapter)
    implementation(libs.play.services.oss)
}

