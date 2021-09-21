import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val applicationId = "com.keelim.nandadiagnosis"
    const val compileSdk = 31
    const val minSdk = 24
    const val targetSdk = 31
    const val buildTools = "30.0.3"
    const val versionCode = 50
    const val versionName = "1.0.50"

    val javaVer = JavaVersion.VERSION_11
    const val javaVerString = "1.8"
}