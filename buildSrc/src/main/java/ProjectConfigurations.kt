import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val applicationId = "com.keelim.nandadiagnosis"
    const val compileSdk = 30
    const val minSdk = 24
    const val targetSdk = 30
    const val buildTools = "30.0.3"
    const val versionCode = 2
    const val versionName = "1.0.2"

    val javaVer = JavaVersion.VERSION_11
    const val javaVerString = "1.8"
}