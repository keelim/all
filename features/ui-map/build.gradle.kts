plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("kapt")
    kotlin("plugin.parcelize")
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
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.material)
    implementation(libs.coil.kt)

    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.ktx)
    implementation(libs.maps.utils.ktx)


}

