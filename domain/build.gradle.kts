plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.data)
    implementation(projects.common)
    implementation(Dep.AndroidX.coreKtx)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.android)
    implementation(Dep.Kotlin.coroutines.core)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}

