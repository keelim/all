plugins {
    alias(libs.plugins.keelim.android.library)
}

android {
    namespace = "com.keelim.web.api"
}

dependencies {
    api(projects.core.common)
    implementation(libs.androidx.browser)
}
