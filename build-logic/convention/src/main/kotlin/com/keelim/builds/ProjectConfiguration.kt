package com.keelim.builds

import org.gradle.api.JavaVersion

object ProjectConfiguration {
    const val compileSdk = 34
    // const val compileSdkExtension = 5
    const val minSdk = 24
    const val targetSdk = 33
    const val versionCode = 20230709
    const val versionName = versionCode.toString()
    val javaVer = JavaVersion.VERSION_17
}
