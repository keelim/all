plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.preferences)
    implementation(libs.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
}
