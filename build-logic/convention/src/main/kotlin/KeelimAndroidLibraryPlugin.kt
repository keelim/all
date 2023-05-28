import com.android.build.api.dsl.LibraryExtension
import com.keelim.builds.ProjectConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.keelim.builds.configureKotlinAndroid

class KeelimAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.getByType<LibraryExtension>().apply {
                buildFeatures.dataBinding = true
                configureKotlinAndroid(this)
            }
        }
    }
}
