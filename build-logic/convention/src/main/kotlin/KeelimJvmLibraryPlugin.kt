import com.keelim.builds.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KeelimJvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("com.jraska.module.graph.assertion")
            }
            configureKotlinJvm()
        }
    }
}
