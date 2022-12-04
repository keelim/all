plugins {
    id("library-setting-plugin")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))
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
android {
    namespace = "com.keelim.cnubus.domain"
}

