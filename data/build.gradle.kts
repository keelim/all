plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:28.1.0"))
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation(AndroidX.core_ktx)
    implementation(LifeCycle.runtime)

    implementation(Hilt.android)
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    implementation("androidx.work:work-runtime-ktx:2.5.0")
    kapt(Hilt.hilt_compiler)

    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)

    implementation(SquareUp.timber)
    implementation(Coroutines.android)

    implementation(SquareUp.core)
    implementation(SquareUp.loggingInterceptor)
    implementation(SquareUp.urlconnection)
    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)

    implementation(DataStore.preferences)

    implementation(Kotlin.stdlibJvm)

    testImplementation(AppTest.junit)
    androidTestImplementation(Room.testing)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Coroutines.test)

    implementation("androidx.hilt:hilt-work:1.0.0-alpha01")
    androidTestImplementation("androidx.work:work-testing:2.4.0")
}

kapt {
    useBuildCache = true
}