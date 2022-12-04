import org.gradle.api.JavaVersion

object ProjectConfigurations {
  const val applicationId = "com.keelim.comssa"
  const val compileSdk = 33
  const val minSdk = 23
  const val targetSdk = 33
  const val buildTools = "32.0.0-rc-1"
  const val versionCode = 9
  const val versionName = "1.0.8"

  val javaVer = JavaVersion.VERSION_11
  const val javaVerString = "1.8"
}
