plugins {
    id("keelim.android.library")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
}

android{
    buildFeatures{
        viewBinding = true
    }
    namespace = "com.keelim.labs"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // activity
    implementation(libs.androidx.appcompat)
    implementation(libs.material3)
    implementation(libs.androidx.core.ktx)
}

