import com.keelim.builds.configureKotlinJvm
import com.keelim.builds.configureJvmJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
class KeelimJvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "java-library")
            apply(plugin = "org.gradle.jacoco")
            apply(plugin = "com.jraska.module.graph.assertion")

            configureKotlinJvm()
            configureJvmJacoco()
        }
    }
}
