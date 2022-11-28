plugins {
    id("library-setting-plugin")
    id("compose-plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.Coil.compose)

    // Compose
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.materialAdapter)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.activity)
}
android {
    namespace = "com.keelim.nandadiagnosis.compose"
}
