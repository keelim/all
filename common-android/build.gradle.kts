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
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation(libs.androidx.lifecycle.process)

    debugImplementation(libs.flipper)
    debugImplementation(libs.soloader)
    debugImplementation(libs.flipper.leakcanary)
    debugImplementation(libs.leakcanary)
    releaseImplementation(libs.flipper.noop)
}
android {
    namespace = "com.keelim.commonAndroid"
}

