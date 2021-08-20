plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(project(":common"))
    implementation(SquareUp.timber)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.activity_ktx)
    implementation(UI.constraintLayout)
    implementation(UI.material)
    implementation(Play.play_location)
    implementation(Play.play_map)
    implementation(Play.maps_sdk)
}