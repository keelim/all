plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
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
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)

    implementation(Dep.Coil.core)

    implementation(Dep.Play.location)
    implementation(Dep.Play.play_map)
    implementation(Dep.Play.maps_sdk)

    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}

