import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.keelim.builds.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class KeelimApplicationJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.gradle.jacoco")
                apply("com.android.application")
            }
            extensions.getByType<ApplicationAndroidComponentsExtension>()
                .also(::configureJacoco)
        }
    }
}
