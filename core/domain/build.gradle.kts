plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    namespace = "com.keelim.domain"
}

dependencies {
    implementation(projects.core.data)
    testImplementation(libs.junit4)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
}
