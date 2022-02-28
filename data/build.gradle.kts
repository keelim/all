import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

val DATA_BASE_URL = gradleLocalProperties(rootDir).getProperty("DATA_BASE_URL")

android{
    buildTypes {
        defaultConfig {
            buildConfigField("String", "DATA_BASE_URL", DATA_BASE_URL)
        }
    }
}

dependencies {
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.datastore.preference)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)
    implementation(Dep.AndroidX.datastore.core)
    implementation(Dep.AndroidX.datastore.preference)

    implementation(Dep.Network.Retrofit.retrofit)
    implementation(Dep.Network.Retrofit.retrofit_gson)
    implementation(Dep.Network.Retrofit.retrofit_moshi)

    implementation(Dep.timber)

    implementation(Dep.Play.maps_sdk)

    implementation(Dep.AndroidX.room.runtime)
    implementation(Dep.AndroidX.room.ktx)
    kapt(Dep.AndroidX.room.compiler)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.play)

    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-storage-ktx")


    implementation(Dep.Network.Retrofit.retrofit_moshi)
    implementation(Dep.Network.Moshi.moshi_kotlin)
    kapt(Dep.Network.Moshi.moshi_codegen)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Dagger.Hilt.test)
}

