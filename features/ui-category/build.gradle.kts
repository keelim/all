plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(project(":data"))
    implementation("com.airbnb.android:epoxy:4.6.2")
    implementation("com.airbnb.android:epoxy-databinding:4.6.2")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.firebase:firebase-common-ktx:20.0.0")
    kapt("com.airbnb.android:epoxy-processor:4.6.2")
    implementation(AndroidX.navigation_ui)
    implementation(AndroidX.navigation_fragment)
    implementation("com.google.firebase:firebase-config-ktx")
}

