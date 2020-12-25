package dependencies

object Versions {
    val androidTargetSdkVersion = 30
    val androidCompileSdkVersion = 30
    val androidMinSdkVersion = 24

    private val versionMajor = 1
    private val versionMinor = 1
    private val versionPatch = 0
    private val versionOffset = 0
    val androidVersionCode =
        ((1 + versionMajor) * 10000 + versionMinor * 100 + versionPatch) * 100 + versionOffset

    val androidVersionName = "$versionMajor.$versionMinor.$versionPatch"
}