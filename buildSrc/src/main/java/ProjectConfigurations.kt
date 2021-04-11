import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val applicationId = "com.keelim.cnubus"
    const val compileSdk = 30
    const val minSdk = 24
    const val targetSdk = 30
    const val buildTools = "31.0.0 rc2"
    const val versionCode = 38
    const val versionName = "2.0.38"

    val javaVer = JavaVersion.VERSION_1_8
    const val javaVerString = "1.8"
}