pluginManagement {
    includeBuild("build-logic")
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
        maven(url = "https://jitpack.io")
    }
}
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
    ":composeApp",
    ":core:common",
    ":core:common-android",
    ":core:component",
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
    ":feature:ui-web",
    ":shared",
    ":widget",
)
