plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("plugin.parcelize")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.keelim.domain"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))

    implementation(libs.androidx.paging.common)
}
