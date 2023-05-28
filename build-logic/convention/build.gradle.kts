
plugins {
    `kotlin-dsl`
}

group = "com.keelim.builds.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "keelim.android.application.compose"
            implementationClass = "KeelimAndroidApplicationComposePlugin"
        }
        register("androidLibraryCompose") {
            id = "keelim.android.library.compose"
            implementationClass = "KeelimAndroidLibraryComposePlugin"
        }
        register("androidTest") {
            id = "keelim.android.test"
            implementationClass = "KeelimAndroidTestPlugin"
        }
        register("androidLibrary") {
            id = "keelim.android.library"
            implementationClass = "KeelimAndroidLibraryPlugin"
        }
        register("androidApplication") {
            id = "keelim.android.application"
            implementationClass = "KeelimAndroidApplicationPlugin"
        }
    }
}

