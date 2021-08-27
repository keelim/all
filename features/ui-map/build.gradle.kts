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
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.fragment_ktx)

    implementation(UI.constraintLayout)
    implementation(UI.recyclerview)
    implementation(UI.material)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(SquareUp.timber)
    implementation(Coroutines.android)

    implementation(Kotlin.stdlibJvm)
    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    androidTestImplementation(Coroutines.test)

    implementation(Play.play_location)
    implementation(Play.play_map)
    implementation(Play.maps_sdk)
}

kapt {
    useBuildCache = true
}