/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keelim.builds

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Compose-specific options
 */
fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            val composeBundle = libs.findBundle("compose").get()
            val composeTestBundle = libs.findBundle("compose-test").get()
            add("implementation", platform(bom))
            add("implementation", composeBundle)
            add("lintChecks", libs.findLibrary("slack-compose-lint").get())
            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", composeTestBundle)
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += buildList {
                // addAll(composeCompilerReports())
                // addAll(composeCompilerMetrics())
                addAll(stabilityConfiguration())
                addAll(strongSkippingConfiguration())
            }
        }
    }
}


private fun Project.composeCompilerReports() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
        project.buildDir.absolutePath + "/compose_compiler"
)

private fun Project.composeCompilerMetrics() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
        project.buildDir.absolutePath + "/compose_compiler"
)

private fun Project.stabilityConfiguration() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${project.rootDir.absolutePath}/compose_compiler_config.conf",
)

private fun Project.strongSkippingConfiguration() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true",
)

