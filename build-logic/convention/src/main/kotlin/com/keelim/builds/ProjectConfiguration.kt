package com.keelim.builds

import org.gradle.api.JavaVersion

object ProjectConfiguration {
    const val compileSdk = 33
    const val minSdk = 24
    const val targetSdk = 33
    const val versionCode = 20230126
    const val versionName = versionCode.toString()
    val javaVer = JavaVersion.VERSION_1_8
}
