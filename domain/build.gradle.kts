plugins {
    id("keelim.android.library")
}

android {
    namespace = "com.keelim.domain"
}

dependencies {
    implementation(project(":data"))
}
