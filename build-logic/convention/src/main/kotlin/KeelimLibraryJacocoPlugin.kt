import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.keelim.builds.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class KeelimLibraryJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.gradle.jacoco")

            extensions.getByType<LibraryAndroidComponentsExtension>()
                .also(::configureJacoco)
        }
    }
}
