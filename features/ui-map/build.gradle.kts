plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
}

android{
    buildFeatures{
        dataBinding = true
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.compose)
    implementation(projects.domain)
    implementation(projects.common)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.fragment.ktx)

    implementation(Dep.AndroidX.UI.recyclerview)
    implementation(Dep.AndroidX.UI.material)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)

    implementation(Dep.Coil.core)

    implementation(Dep.Play.location)
    implementation(Dep.Play.play_map)
    implementation(Dep.Play.maps_sdk)
    implementation(Dep.Play.maps_utils)

    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}

