plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.keelim.comssa.domain"
}

dependencies {
    implementation(projects.data)
    implementation(AndroidX.core_ktx)
    implementation(Hilt.android)
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    kapt(Hilt.hilt_compiler)

    implementation(SquareUp.timber)
    implementation(Kotlin.Coroutines.android)

    implementation(Kotlin.stdlibJvm)
    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Kotlin.Coroutines.test)
}
