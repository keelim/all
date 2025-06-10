plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
}

android{
    namespace = "com.keelim.scheme"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.composeCore)
    implementation(libs.androidx.activity.compose)
}

