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

dependencies {
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(DataStore.preferences)
    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(SquareUp.core)
    implementation(SquareUp.loggingInterceptor)
    implementation(SquareUp.urlconnection)
    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)

    implementation(SquareUp.timber)
    implementation(Coroutines.android)

    implementation(Kotlin.stdlibJvm)
    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Coroutines.test)
    implementation(Play.maps_sdk)
}