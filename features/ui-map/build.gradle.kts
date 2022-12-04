plugins {
    id("keelim.android.library")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
}

android{
    buildFeatures{
        dataBinding = true
    }
    namespace = "com.keelim.map"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

