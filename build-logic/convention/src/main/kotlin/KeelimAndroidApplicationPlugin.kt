import com.android.build.api.dsl.ApplicationExtension
import com.keelim.builds.ProjectConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.keelim.builds.configureAndroidCompose
import com.keelim.builds.configureKotlinAndroid
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies

class KeelimAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
                apply("com.google.android.gms.oss-licenses-plugin")
                apply("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
                apply("org.jetbrains.qodana")
            }


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                defaultConfig.versionName = ProjectConfiguration.versionName
                defaultConfig.versionCode = ProjectConfiguration.versionCode
                defaultConfig.targetSdk = ProjectConfiguration.targetSdk
                buildFeatures.dataBinding = true
                buildTypes.getByName("release").apply {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("debugImplementation", libs.findLibrary("flipper").get())
                add("debugImplementation", libs.findLibrary("soloader").get())
                add("releaseImplementation", libs.findLibrary("flipper-noop").get())
            }
        }
    }
}
