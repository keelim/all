enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  val agpVersion = "7.3.1"
  val kotlinVersion = "1.7.20"

  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
  plugins {
    id("com.android.application") version agpVersion
    id("com.android.library") version agpVersion
    id("org.jetbrains.kotlin.android") version kotlinVersion
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    id("org.jetbrains.kotlin.parcelize") version kotlinVersion
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.7"
  }
  resolutionStrategy {
    eachPlugin {
      when (val id = requested.id.id) {
        "dagger.hilt.android.plugin" ->
          useModule("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
        "org.jetbrains.kotlin.plugin.serialization" ->
          useModule("${id}:${id}.gradle.plugin:${kotlinVersion}")
        "com.google.android.gms.oss-licenses-plugin" ->
          useModule("com.google.android.gms:oss-licenses-plugin:0.10.5")
      }
    }
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
  }
}

rootProject.name = "comssa"

include(":app", ":data", ":domain")
