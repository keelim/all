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
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.coil.kt)
    implementation(libs.fragment.ktx)
    implementation(libs.hilt.android.testing)
    implementation(libs.junit4)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.material)
    implementation(libs.material)
    implementation(libs.material.themAdapter)
    implementation(libs.play.services.oss)
}

