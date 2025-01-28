plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    namespace = "com.keelim.commonAndroid"
}

dependencies {
    implementation(projects.core.composeCore)
    
    api(projects.core.common)
    api(projects.shared)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common)
    implementation(libs.firebase.crashlytics)
    releaseImplementation(libs.firebase.appcheck)
    debugImplementation(libs.firebase.appcheck.debug)

    implementation(libs.hilt.android)
    implementation(libs.junit4)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.ad)
    implementation(libs.timber)

    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.leakcanary)
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.soloader)

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hilt.android.testing)
}

