plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.keelim.android.library.jacoco)
}

android {
    namespace = "com.keelim.domain"
}

dependencies {
    implementation(projects.core.dataApi)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.property)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
}
