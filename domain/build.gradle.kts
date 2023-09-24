plugins {
    id("keelim.android.library")
    id("keelim.android.hilt")
}

android {
    namespace = "com.keelim.domain"
}

dependencies {
    implementation(project(":data"))
    testImplementation(libs.junit4)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
}
