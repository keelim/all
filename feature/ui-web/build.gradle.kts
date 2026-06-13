plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
}

android {
    namespace = "com.keelim.web"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.browser)
}
