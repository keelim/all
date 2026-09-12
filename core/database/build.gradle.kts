plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.keelim.android.application.room)
}

android { namespace = "com.keelim.database" }

dependencies {
    implementation(projects.core.model)
    implementation(projects.shared)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(projects.core.testing)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testRuntimeOnly(libs.androidx.sqlite.bundled.jvm)
}

// Local unit tests need host SQLite natives, not the Android JNI loader.
configurations.matching { it.name.endsWith("UnitTestRuntimeClasspath") }.configureEach {
    exclude(group = "androidx.sqlite", module = "sqlite-bundled-android")
}
