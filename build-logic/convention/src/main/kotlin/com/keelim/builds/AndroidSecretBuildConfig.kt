package com.keelim.builds

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import groovy.json.JsonOutput
import java.util.Properties
import org.gradle.api.Project

/** Keep an optional Maps key valid without changing properties or manifest placeholders. */
internal fun Project.configureEmptySecretBuildConfig() {
    pluginManager.withPlugin("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") {
        val mapsKeyLiteral = providers.fileContents(
            rootProject.layout.projectDirectory.file("local.properties"),
        ).asText.orElse("").map { content ->
            val properties = Properties().apply { load(content.reader()) }
            JsonOutput.toJson(properties.getProperty("MAPS_API_KEY", "").removeSurrounding("\""))
        }
        extensions.getByType(AndroidComponentsExtension::class.java).onVariants { variant ->
            // Secrets plugin 2.0.1 leaves empty values unquoted in generated Java.
            // Keep this lazy: variant fields cannot be queried during configuration.
            variant.buildConfigFields?.put("MAPS_API_KEY", mapsKeyLiteral.map { literal ->
                BuildConfigField("String", literal, "Optional Maps API key")
            })
        }
    }
}
