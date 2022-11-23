plugins {
    id("library-setting-plugin")
    id("compose-setting-plugin")
    kotlin("kapt")
}

dependencies {
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.Coil.core)
    implementation(Dep.Coil.compose)
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.materialAdapter)
    implementation(Dep.AndroidX.Compose.glance)

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Kotlin.coroutines.test)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.AndroidX.Compose.test)
}
android {
    namespace = "com.keelim.compose"
}

