import com.google.devtools.ksp.gradle.KspExtension
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KeelimShowkasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                arg("skipPrivatePreviews", "true")
            }

            dependencies {
                add("implementation", libs.findLibrary("showkase").get())
                add("ksp", libs.findLibrary("showkase-processor").get())
            }
        }
    }
}
