plugins {
    id("keelim.android.library")
    id("keelim.android.hilt")
}

android {
    namespace = "com.keelim.commonAndroid"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    implementation(libs.coil.kt.svg)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.junit4)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.play.services.ad)
    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)

    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.leakcanary)
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.soloader)

    releaseImplementation(libs.flipper.noop)
}

