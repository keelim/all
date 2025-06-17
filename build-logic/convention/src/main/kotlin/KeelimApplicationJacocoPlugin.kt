
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.keelim.builds.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class KeelimApplicationJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.gradle.jacoco")
            extensions.getByType<ApplicationAndroidComponentsExtension>()
                .also(::configureJacoco)
        }
    }
}
