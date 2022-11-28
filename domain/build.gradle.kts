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
    implementation(Dep.AndroidX.paging.common)
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)


    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.core)
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.timber)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}
android {
    namespace = "com.keelim.nandadiagnosis.domain"
}
