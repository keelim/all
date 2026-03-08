plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    namespace = "com.keelim.commonAndroid"
}

dependencies {

    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.shared)
    implementation(projects.core.dataApi)
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.ad)
    implementation(libs.timber)

    debugImplementation(libs.leakcanary)

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hilt.android.testing)

    implementation(libs.play.services.auth)
    implementation(libs.play.services.auth.api.phone)
}
