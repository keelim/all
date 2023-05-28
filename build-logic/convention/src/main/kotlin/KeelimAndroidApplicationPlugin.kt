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
            }


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                defaultConfig.versionName = ProjectConfiguration.versionName
                defaultConfig.versionCode = ProjectConfiguration.versionCode
                defaultConfig.targetSdk = ProjectConfiguration.targetSdk
                buildFeatures.dataBinding = true
                buildTypes.getByName("release").apply {
                    isMinifyEnabled = true
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
