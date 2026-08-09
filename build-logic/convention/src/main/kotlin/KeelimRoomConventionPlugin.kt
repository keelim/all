/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import androidx.room.gradle.RoomExtension
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class KeelimRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "androidx.room")

            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            val roomRuntime = libs.findLibrary("room.runtime").get()
            val bundledSqlite = libs.findLibrary("androidx.sqlite.bundled").get()
            val roomTesting = libs.findLibrary("room.testing").get()
            val roomCompiler = libs.findLibrary("room.compiler").get()

            listOf("com.android.application", "com.android.library").forEach { pluginId ->
                pluginManager.withPlugin(pluginId) {
                    addDependencyIfConfigExists("implementation", roomRuntime)
                    addDependencyIfConfigExists("implementation", bundledSqlite)
                    addDependencyIfConfigExists("androidTestImplementation", roomTesting)
                    addDependencyIfConfigExists("ksp", roomCompiler)
                }
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                addDependencyIfConfigExists("commonMainImplementation", roomRuntime)
                addDependencyIfConfigExists("commonMainImplementation", bundledSqlite)
                addDependencyIfConfigExists("androidTestImplementation", roomTesting)
                addDependencyIfConfigExists("androidInstrumentedTestImplementation", roomTesting)
                addDependencyIfConfigExists("kspAndroid", roomCompiler)
                addDependencyIfConfigExists("kspCommonMainMetadata", roomCompiler)
            }
        }
    }

    private fun Project.addDependencyIfConfigExists(configuration: String, dependencyNotation: Any) {
        if (configurations.findByName(configuration) != null) {
            dependencies.add(configuration, dependencyNotation)
        }
    }
}
