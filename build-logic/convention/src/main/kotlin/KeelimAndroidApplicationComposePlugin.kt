
import com.android.build.api.dsl.ApplicationExtension
import com.keelim.builds.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class KeelimAndroidApplicationComposePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidCompose(
                commonExtension = extensions.getByType<ApplicationExtension>(),
                composeCompilerGradlePluginExtension = extensions.getByType<ComposeCompilerGradlePluginExtension>()
            )
        }
    }
}
