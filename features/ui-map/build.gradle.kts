plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.library.compose")
    id("keelim.android.hilt")
    kotlin("kapt")
    kotlin("plugin.parcelize")
}

android{
    namespace = "com.keelim.map"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.kt)
    implementation(libs.maps.ktx)
    implementation(libs.maps.utils.ktx)
    implementation(libs.material)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
}

