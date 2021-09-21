plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
//    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(project(":data"))
}

kapt {
    useBuildCache = true
}

