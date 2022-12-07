plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    kotlin("kapt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.fragment.ktx)
    implementation(libs.material)

    // compose
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.material.themAdapter)

    implementation(libs.coil.kt)
    implementation(libs.play.services.oss)
}
android {
    namespace = "com.keelim.common"
}

