import Glide.moshi
import Glide.moshi_codegen
import Glide.moshi_kotlin
import SquareUp.retrofit_moshi

plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments.plus(mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                ))
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:30.5.0"))
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation(AndroidX.core_ktx)

    implementation(Hilt.android)
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    kapt(Hilt.hilt_compiler)

    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)

    implementation(SquareUp.timber)
    implementation(Kotlin.Coroutines.android)
    implementation(Kotlin.Coroutines.play)

    implementation(Kotlin.stdlibJvm)

    testImplementation(AppTest.junit)
    androidTestImplementation(Room.testing)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Kotlin.Coroutines.test)

    implementation(SquareUp.core)
    implementation(SquareUp.loggingInterceptor)
    implementation(SquareUp.urlconnection)
    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)
    implementation(retrofit_moshi)
    implementation(moshi)
    implementation(moshi_kotlin)
    kapt(moshi_codegen)
}
