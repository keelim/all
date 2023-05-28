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
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))

    // activity
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.arrow.core)
}

