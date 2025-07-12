import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.keelim.builds.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.androidx.benchmark.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.dependencyGuard.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.androidx.benchmark.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.keelim.android.application.asProvider().get().pluginId
            implementationClass = "KeelimAndroidApplicationPlugin"
        }
        register("androidApplicationFirebase") {
            id = libs.plugins.keelim.android.application.firebase.get().pluginId
            implementationClass = "KeelimApplicationFirebasePlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.keelim.android.application.compose.get().pluginId
            implementationClass = "KeelimAndroidApplicationComposePlugin"
        }
        register("androidApplicationJacoco") {
            id = libs.plugins.keelim.android.application.jacoco.get().pluginId
            implementationClass = "KeelimApplicationJacocoPlugin"
        }
        register("androidApplicationRoom") {
            id = libs.plugins.keelim.android.application.room.get().pluginId
            implementationClass = "KeelimRoomConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.keelim.android.library.asProvider().get().pluginId
            implementationClass = "KeelimAndroidLibraryPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.keelim.android.library.compose.get().pluginId
            implementationClass = "KeelimAndroidLibraryComposePlugin"
        }
        register("androidLibraryJacoco") {
            id = libs.plugins.keelim.android.library.jacoco.get().pluginId
            implementationClass = "KeelimLibraryJacocoPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.keelim.jvm.library.get().pluginId
            implementationClass = "KeelimJvmLibraryPlugin"
        }
        register("androidTest") {
            id = libs.plugins.keelim.android.test.get().pluginId
            implementationClass = "KeelimAndroidTestPlugin"
        }
        register("androidHilt") {
            id = libs.plugins.keelim.android.hilt.get().pluginId
            implementationClass = "KeelimHiltPlugin"
        }
        register("showkase") {
            id = libs.plugins.keelim.android.showkase.get().pluginId
            implementationClass = "KeelimShowkasePlugin"
        }
        register("multiplatform") {
            id = libs.plugins.keelim.multiplatform.get().pluginId
            implementationClass = "KeelimMultiPlatformConventionPlugin"
        }
    }
}

