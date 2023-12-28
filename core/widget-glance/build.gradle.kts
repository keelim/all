plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
}

android {
    namespace = "com.keelim.widget.glance"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.androidx.compose.glance)
    implementation(libs.androidx.compose.glance.material3)
    implementation(libs.androidx.compose.ui.tooling)
}
