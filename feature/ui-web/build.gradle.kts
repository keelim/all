plugins {
    alias(libs.plugins.keelim.android.library)
}

android {
    namespace = "com.keelim.web"
}

dependencies {
    implementation(libs.androidx.browser)
}

