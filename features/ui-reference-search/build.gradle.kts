plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

listOf(
        "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}


dependencies {
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)
    implementation(UI.material)
    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)
}
