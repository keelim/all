plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
//    id("dagger.hilt.android.plugin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
}

kapt {
    useBuildCache = true
}

