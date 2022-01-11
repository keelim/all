plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.data)
    implementation(projects.common)
    implementation(AndroidX.core_ktx)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(SquareUp.timber)
    implementation(Kotlin.stdlibJvm)
    implementation(Coroutines.android)
    implementation(Coroutines.core)

    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Coroutines.test)
}

