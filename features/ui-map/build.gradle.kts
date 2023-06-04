plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    id("org.jetbrains.kotlin.android")
}

android{
    namespace = "com.keelim.map"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt)
    implementation(libs.maps.ktx)
    implementation(libs.maps.utils.ktx)
    implementation(libs.material)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
}

