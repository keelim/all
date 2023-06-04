plugins {
    id("keelim.android.library")
    id("keelim.android.hilt")
}

android {
    namespace = "com.keelim.commonAndroid"
}

dependencies {
    implementation(project(":common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.startup)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    implementation(libs.coil.kt.svg)
    implementation(libs.play.services.ad)
    implementation(libs.timber)

    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.leakcanary)
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.soloader)

    releaseImplementation(libs.flipper.noop)
}

