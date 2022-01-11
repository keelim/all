plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android{
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.compose)
    implementation(projects.domain)
    implementation(projects.common)

    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.work)
    implementation(Dep2.Compose.liveData)

    implementation(UI.constraintLayout)
    implementation(UI.recyclerview)
    implementation(UI.material)


    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(SquareUp.timber)

    implementation(Coil.coil)

    implementation(Play.play_location)
    implementation(Play.play_map)
    implementation(Play.maps_sdk)

    implementation(Kotlin.stdlibJvm)
    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Coroutines.test)
}

