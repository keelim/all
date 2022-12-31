import com.android.build.api.dsl.ApplicationExtension
import com.keelim.builds.ProjectConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.keelim.builds.configureAndroidCompose
import com.keelim.builds.configureKotlinAndroid

class KeelimAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = ProjectConfiguration.targetSdk
                buildFeatures.viewBinding = true
                buildTypes.getByName("release").apply {
                    isMinifyEnabled = true
                }
            }
        }
    }
}
