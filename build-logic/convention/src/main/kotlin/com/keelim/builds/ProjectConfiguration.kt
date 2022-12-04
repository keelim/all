package com.keelim.builds

import org.gradle.api.JavaVersion

object ProjectConfiguration {
    const val compileSdk = 33
    const val minSdk = 24
    const val targetSdk = 32
    val javaVer = JavaVersion.VERSION_1_8
}
