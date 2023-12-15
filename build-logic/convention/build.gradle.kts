import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.keelim.builds.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.androidx.benchmark.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
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
            id = "keelim.android.application"
            implementationClass = "KeelimAndroidApplicationPlugin"
        }
        register("androidApplicationFirebase") {
            id = "keelim.android.application.firebase"
            implementationClass = "KeelimApplicationFirebasePlugin"
        }
        register("androidApplicationCompose") {
            id = "keelim.android.application.compose"
            implementationClass = "KeelimAndroidApplicationComposePlugin"
        }
        register("androidApplicationJacoco") {
            id = "keelim.android.application.jacoco"
            implementationClass = "KeelimApplicationJacocoPlugin"
        }
        register("androidApplicationRoom") {
            id = "keelim.android.application.room"
            implementationClass = "KeelimRoomConventionPlugin"
        }
        register("androidLibrary") {
            id = "keelim.android.library"
            implementationClass = "KeelimAndroidLibraryPlugin"
        }
        register("androidLibraryCompose") {
            id = "keelim.android.library.compose"
            implementationClass = "KeelimAndroidLibraryComposePlugin"
        }
        register("androidLibraryJacoco") {
            id = "keelim.android.library.jacoco"
            implementationClass = "KeelimLibraryJacocoPlugin"
        }
        register("androidTest") {
            id = "keelim.android.test"
            implementationClass = "KeelimAndroidTestPlugin"
        }
        register("androidHilt") {
            id = "keelim.android.hilt"
            implementationClass = "KeelimHiltPlugin"
        }
        register("showkase") {
            id = "keelim.android.showkase"
            implementationClass = "KeelimShowkasePlugin"
        }
    }
}

