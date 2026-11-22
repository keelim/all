import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.keelim.appfunction"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.androidx.appfunctions)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    ksp(libs.androidx.appfunctions.compiler)

    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)
    testRuntimeOnly(libs.junit.vintage.engine)
}

// Robolectric 4.16.1 supports the AppFunctions minimum API 36 on a Java 21 runner.
// Android/Kotlin compilation continues to target the repository's Java 17 baseline.
val testJavaToolchains = extensions.getByType<JavaToolchainService>()
tasks.withType<Test>().configureEach {
    javaLauncher.set(testJavaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    })
}
