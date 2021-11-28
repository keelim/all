import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val applicationId = "com.keelim.comssa"
    const val compileSdk = 31
    const val minSdk = 24
    const val targetSdk = 31
    const val buildTools = "32.0.0-rc-1"
    const val versionCode = 2
    const val versionName = "1.0.2"

    val javaVer = JavaVersion.VERSION_11
    const val javaVerString = "1.8"
}