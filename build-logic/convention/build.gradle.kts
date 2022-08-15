
plugins {
    `kotlin-dsl`
}

group = "com.keelim.builds.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("spotless") {
            id = "keelim.spotless.lint"
            implementationClass = "SpotlessConventionPlugin"
        }
    }
}

