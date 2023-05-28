import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.keelim.builds.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class KeelimLibraryJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.gradle.jacoco")
                apply("com.android.library")
            }
            extensions.getByType<LibraryAndroidComponentsExtension>()
                .also(::configureJacoco)
        }
    }
}
