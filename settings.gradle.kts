pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
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
    ":feature:ui-labs",
    ":feature:ui-setting",
    ":shared",
    ":widget",
)
