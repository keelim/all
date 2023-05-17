plugins {
    id("keelim.android.library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    implementation(libs.timber)
    implementation(libs.play.services.ad)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.compose.ui)

    debugImplementation(libs.flipper)
    debugImplementation(libs.soloader)
    debugImplementation(libs.flipper.leakcanary)
    debugImplementation(libs.leakcanary)
    releaseImplementation(libs.flipper.noop)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
android {
    namespace = "com.keelim.commonAndroid"
}

