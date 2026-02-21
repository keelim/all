
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
            val isCiBuild = providers.environmentVariable("CI")
                .map { it.equals("true", ignoreCase = true) }
                .orElse(false)
                .get()

            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "org.gradle.android.cache-fix")
            // OSS licenses plugin is not configuration-cache compatible.
            // Apply it only for release/publish paths or when explicitly requested.
            if (shouldApplyOssLicensesPlugin()) {
                apply(plugin = "com.google.android.gms.oss-licenses-plugin")
            }
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
                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = false
                        isShrinkResources = false
                        isCrunchPngs = false
                    }
                    getByName("release") {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                lint {
                    abortOnError = isCiBuild
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

    private fun Project.shouldApplyOssLicensesPlugin(): Boolean {
        val enabledByProperty = providers.gradleProperty("enableOssLicenses")
            .orNull
            ?.toBooleanStrictOrNull() == true
        if (enabledByProperty) {
            return true
        }

        return gradle.startParameter.taskNames.any { taskName ->
            val normalized = taskName.lowercase()
            normalized.contains("bundlerelease") ||
                normalized.contains("assemblerelease") ||
                normalized.contains("publish") ||
                normalized.contains("upload")
        }
    }
}
