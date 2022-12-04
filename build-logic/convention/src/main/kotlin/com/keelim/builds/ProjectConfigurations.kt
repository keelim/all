package com.keelim.builds

import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val compileSdk = 33
    const val minSdk = 24
    const val targetSdk = 32
    const val buildTools = "31.0.0"
    const val applicationID = "com.keelim.mygrade"
    const val versionCode = 20
    const val versionName = "0.0.20"
    val javaVer = JavaVersion.VERSION_11
}
