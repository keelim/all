plugins {
    id("keelim.android.library")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}


dependencies {
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
android {
    namespace = "com.keelim.ui_setting"
}
