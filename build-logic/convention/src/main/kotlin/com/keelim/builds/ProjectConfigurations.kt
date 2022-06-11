package com.keelim.builds

import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val compileSdk = 31
    const val minSdk = 24
    const val targetSdk = 31
    const val buildTools = "31.0.0"
    const val applicationID = "com.keelim.mygrade"
    const val versionCode = 11
    const val versionName = "0.0.11"
    val javaVer = JavaVersion.VERSION_11
}
