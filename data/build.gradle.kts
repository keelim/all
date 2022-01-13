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
                arguments.plus(
                    mapOf(
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
    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.lifecycle.runtime)

    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.AndroidX.datastore.core)
    implementation(Dep.AndroidX.datastore.preference)
    kapt(Dep.Dagger.Hilt.compiler)
    implementation(Dep.AndroidX.hilt.work)

    implementation(Dep.AndroidX.paging.common)
    implementation(Dep.AndroidX.WorkManager.work)

    implementation(Dep.AndroidX.room.runtime)
    implementation(Dep.AndroidX.room.ktx)
    kapt(Dep.AndroidX.room.compiler)

    implementation(Dep.Kotlin.coroutines.android)
    kapt(Dep.Kotlin.coroutines.core)

    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)
    implementation(Dep.Network.Retrofit.retrofit)
    implementation(Dep.Network.Retrofit.retrofit_moshi)
    implementation(Dep.Network.Retrofit.retrofit_gson)
    implementation(Dep.timber)
    implementation(Dep.AndroidX.appcompat)


    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}