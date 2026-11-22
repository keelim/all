plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.keelim.appfunction.api"
}

dependencies {
    implementation(libs.androidx.appfunctions)
    ksp(libs.androidx.appfunctions.compiler)
}
