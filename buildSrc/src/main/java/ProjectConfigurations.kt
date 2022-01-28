import org.gradle.api.JavaVersion

object
ProjectConfigurations {
    const val applicationId = "com.keelim.cnubus"
    const val compileSdk = 31
    const val minSdk = 24
    const val targetSdk = 31
    const val buildTools = "30.0.3"
    const val versionCode = 2104
    const val versionName = "2.1.04"

    val javaVer = JavaVersion.VERSION_11
    const val javaVerString = "11"

    const val composeCompiler = "1.0.5"
}