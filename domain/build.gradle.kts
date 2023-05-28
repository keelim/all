plugins {
    id("keelim.android.library")
    id("keelim.android.hilt")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))

    implementation(libs.androidx.paging.common)
}
android {
    namespace = "com.keelim.domain"
}
