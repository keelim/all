
import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import com.android.build.api.dsl.ApplicationExtension
import com.keelim.builds.configureDependencyGuard
import com.keelim.builds.configureKotlinAndroid
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class KeelimAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "org.gradle.android.cache-fix")
            apply(plugin = "com.google.android.gms.oss-licenses-plugin")
            apply(plugin = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
            apply(plugin = "com.dropbox.dependency-guard")
            apply(plugin = "com.jraska.module.graph.assertion")
            apply(plugin = "androidx.baselineprofile")
            apply(plugin = "com.autonomousapps.dependency-analysis")


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                configureDependencyGuard()
                defaultConfig {
                    versionName = libs.findVersion("versionCode").get().displayName
                    versionCode = libs.findVersion("versionCode").get().displayName.toInt()
                    targetSdk = libs.findVersion("targetSdk").get().displayName.toInt()
                }
                with(buildFeatures) {
                    buildConfig = true
                }
                buildTypes.getByName("release").apply {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    vcsInfo.include = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
                lint {
                    abortOnError = false
                }
            }

            configure<BaselineProfileConsumerExtension> {
                dexLayoutOptimization = true
                automaticGenerationDuringBuild = true
            }

            dependencies {
                add("lintChecks", libs.findLibrary("slack-lint-checks").get())
                add("lintChecks", libs.findLibrary("insights-lint").get())
                add("implementation", libs.findLibrary("androidx-tracing-ktx").get())
            }
        }
    }
}
