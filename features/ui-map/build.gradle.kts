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
        viewBinding = true
    }
    namespace = "com.keelim.map"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

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

