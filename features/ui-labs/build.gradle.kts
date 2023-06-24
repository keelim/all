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
    implementation(project(":common"))
    implementation(project(":compose:compose-core"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.arrow.core)
    implementation(libs.material)
}

