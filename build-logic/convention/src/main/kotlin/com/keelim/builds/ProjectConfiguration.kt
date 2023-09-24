package com.keelim.builds

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

object ProjectConfiguration {
    const val compileSdk = 34
    // const val compileSdkExtension = 5
    const val minSdk = 26
    const val targetSdk = 33
    const val versionCode = 20230924
    const val versionName = versionCode.toString()
    val javaVer = JavaVersion.VERSION_17
}

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
