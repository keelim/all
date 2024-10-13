plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
}

android {
    namespace = "com.keelim.widget.glance"
}

dependencies {
    api(libs.androidx.compose.glance.widget)
    api(libs.androidx.compose.glance)
}
