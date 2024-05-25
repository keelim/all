
import com.android.build.api.dsl.LibraryExtension
import com.keelim.builds.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class KeelimAndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidCompose(
                commonExtension = extensions.getByType<LibraryExtension>(),
                composeCompilerGradlePluginExtension = extensions.getByType<ComposeCompilerGradlePluginExtension>()
            )
        }
    }
}
