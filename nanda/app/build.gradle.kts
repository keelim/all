plugins {
  id("application-setting-plugin")
  id("kotlin-kapt")
  id("com.google.gms.google-services") version ("4.3.14")
  id("com.google.firebase.crashlytics") version ("2.9.0")
  id("androidx.navigation.safeargs.kotlin") version ("2.4.2")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version ("2.0.1")
  id("dagger.hilt.android.plugin")
  id("com.google.android.gms.oss-licenses-plugin")
}

android {
  defaultConfig {
    applicationId = ProjectConfigurations.applicationId
    versionCode = ProjectConfigurations.versionCode
    versionName = ProjectConfigurations.versionName
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  useLibrary("android.test.mock")
  buildFeatures { dataBinding = true }
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
  implementation(projects.common)
  implementation(projects.compose)
  implementation(projects.data)
  implementation(projects.domain)
  implementation(projects.features.uiSetting)
  implementation(projects.features.player)

  implementation(platform(Dep.Firebase.platform))
  implementation("com.google.firebase:firebase-core")
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-database-ktx")
  implementation("com.google.firebase:firebase-crashlytics-ktx")
  implementation("com.google.firebase:firebase-messaging-ktx")
  implementation("com.google.firebase:firebase-analytics-ktx")
  implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
  implementation("com.google.firebase:firebase-config-ktx")

  implementation(Dep.AndroidX.activity.ktx)
  implementation(Dep.AndroidX.fragment.ktx)
  implementation(Dep.AndroidX.coreKtx)
  implementation(Dep.AndroidX.navigation.ui)
  implementation(Dep.AndroidX.navigation.fragment)

  implementation(Dep.AndroidX.lifecycle.runtime)
  implementation(Dep.AndroidX.lifecycle.livedata)

  implementation(Dep.AndroidX.UI.material)
  implementation(Dep.AndroidX.UI.recyclerview)
  implementation(Dep.AndroidX.UI.preference)
  implementation(Dep.AndroidX.UI.recycler_selection)
  implementation(Dep.AndroidX.UI.swiperefreshlayout)

  implementation(Dep.Play.ad)

  implementation(Dep.Hilt.android)
  kapt(Dep.Hilt.hilt_compiler)

  implementation(Dep.AndroidX.paging.runtime)
  implementation(Dep.AndroidX.paging.common)
  implementation(Dep.Play.ad)
  implementation(Dep.Play.core)
  implementation(Dep.Play.oss)
  implementation(Dep.Play.play_auth)

  implementation(Dep.Network.Retrofit.retrofit)

  implementation(Dep.Coil.core)

  // Compose
  implementation(Dep.AndroidX.Compose.material)
  implementation(Dep.AndroidX.Compose.tooling)
  implementation(Dep.AndroidX.Compose.activity)

  implementation(Dep.AndroidX.hilt.work)
  implementation(Dep.AndroidX.hilt.common)
  implementation(Dep.AndroidX.WorkManager.work)

  implementation(Dep.AndroidX.startup.runtime)

  implementation(Dep.timber)

  testImplementation(Dep.Test.junit)
  androidTestImplementation(Dep.Test.androidJunit)
  androidTestImplementation(Dep.Test.espressoCore)
}
