plugins {
    id("keelim.android.library")
    kotlin("kapt")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    implementation(libs.timber)
    implementation(libs.play.services.ad)
}
android {
    namespace = "com.keelim.common_android"
}

