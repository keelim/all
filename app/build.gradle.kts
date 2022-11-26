plugins {
  id("application-setting-plugin")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("com.google.gms.google-services") version ("4.3.14")
  id("com.google.firebase.crashlytics") version ("2.9.0")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs.kotlin") version ("2.4.2")
  id("com.google.android.gms.oss-licenses-plugin")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version ("2.0.1")
}

android {
  defaultConfig {
    applicationId = ProjectConfigurations.applicationId
    versionCode = ProjectConfigurations.versionCode
    versionName = ProjectConfigurations.versionName
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    defaultConfig {}
    getByName("release") {}
  }
  composeOptions { kotlinCompilerExtensionVersion = "1.3.2" }
  namespace = "com.keelim.comssa"
}

dependencies {
  implementation(projects.data)
  implementation(projects.domain)
  implementation("androidx.core:core-ktx:1.6.0")
  implementation("androidx.appcompat:appcompat:1.3.0")
  implementation("com.google.android.material:material:1.4.0")
  implementation("androidx.activity:activity-ktx:1.6.1")
  implementation("androidx.fragment:fragment-ktx:1.5.4")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0")
  implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
  implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
  implementation("androidx.lifecycle:lifecycle-process:2.3.1")
  implementation("com.google.android.play:core-ktx:1.8.1")

  implementation(platform("com.google.firebase:firebase-bom:30.5.0"))
  implementation("com.google.firebase:firebase-analytics-ktx")
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-crashlytics-ktx")
  implementation("com.google.firebase:firebase-perf-ktx")
  implementation("androidx.paging:paging-common-ktx:3.1.0")
  implementation("androidx.paging:paging-runtime-ktx:3.1.0")

  implementation(Kotlin.stdlibJvm)

  implementation("com.jakewharton.timber:timber:5.0.1")

  implementation("com.google.android.gms:play-services-ads:20.5.0")

  implementation(Hilt.android)
  kapt(Hilt.hilt_compiler)

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

  implementation(Coil.coil)
  implementation(Coil.coilGif)
  implementation(AndroidX.startup)

  // compose
  val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
  implementation("androidx.compose.ui:ui")
  implementation(composeBom)
  androidTestImplementation(composeBom)

  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.material:material")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  debugImplementation("androidx.compose.ui:ui-tooling")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
  implementation("androidx.compose.material:material-icons-extended")
  implementation("androidx.compose.material3:material3-window-size-class")
  implementation("androidx.activity:activity-compose:1.5.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
  implementation("androidx.compose.runtime:runtime-livedata")
}
