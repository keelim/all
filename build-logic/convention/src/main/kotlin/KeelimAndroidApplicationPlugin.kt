import com.android.build.api.dsl.ApplicationExtension
import com.keelim.builds.ProjectConfiguration
import com.keelim.builds.configureKotlinAndroid
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KeelimAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
                apply("com.google.android.gms.oss-licenses-plugin")
                apply("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
            }


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                defaultConfig {
                    versionName = ProjectConfiguration.versionName
                    versionCode = ProjectConfiguration.versionCode
                    targetSdk = ProjectConfiguration.targetSdk
                }
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

            dependencies {
                add("debugImplementation", libs.findLibrary("flipper").get())
                add("debugImplementation", libs.findLibrary("soloader").get())
                add("releaseImplementation", libs.findLibrary("flipper-noop").get())
            }
        }
    }
}
