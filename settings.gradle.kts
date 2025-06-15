import com.facebook.react.ReactSettingsExtension

pluginManagement {
    includeBuild("build-logic")
    includeBuild("feature/rn/node_modules/@react-native/gradle-plugin")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13511472/artifacts/repository")
        }
    }
}
plugins { id("com.facebook.react.settings") }
extensions.configure<ReactSettingsExtension> {
    autolinkLibrariesFromCommand(
        workingDirectory = file("feature/rn")
    )
}
includeBuild("feature/rn/node_modules/@react-native/gradle-plugin")

rootProject.name = "all"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":app-arducon",
    ":app-cnubus",
    ":app-my-grade",
    ":app-nanda",
    ":app-comssa",
    ":benchmarks",
    // ":catalog",
    ":core:common",
    ":core:common-android",
    ":core:compose-core",
    ":core:data",
    ":core:data-api",
    ":core:database",
    ":core:datastore-proto",
    ":core:domain",
    ":core:model",
    ":core:network",
    ":core:resource",
    ":core:testing",
    ":core:navigation",
    ":feature:ui-scheme",
    ":feature:ui-setting",
    ":shared",
    ":widget",
)
