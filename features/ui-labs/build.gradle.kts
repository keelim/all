plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("plugin.parcelize")
}

android{
    namespace = "com.keelim.labs"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.composeCore)
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}

