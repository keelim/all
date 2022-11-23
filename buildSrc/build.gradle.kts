plugins {
    `kotlin-dsl`
}

group = "com.keelim.buildSrc"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.3.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.squareup:javapoet:1.13.0")
}

gradlePlugin {
    plugins {
        register("firebase-set") {
            id = "keelim.firebase-set"
            implementationClass = "FirebaseSetConventionPlugin"
        }
    }
}
