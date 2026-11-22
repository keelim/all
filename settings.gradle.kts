pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroup("org.chromium.net")
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
                includeGroup("org.chromium.net")
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
    ":app-mysenior",
    ":benchmarks",
    ":composeApp",
    ":core:common",
    ":core:component",
    ":core:designsystem",
    ":core:device",
    ":core:data:data-api",
    ":core:data:data-impl",
    ":core:database",
    ":core:datastore-proto",
    ":core:domain",
    ":core:model",
    ":core:network",
    ":core:resource",
    ":core:testing",
    ":core:navigation",
    ":feature:app-function:api",
    ":feature:app-function:impl",
    ":feature:ui-a2ui:api",
    ":feature:ui-a2ui:impl",
    ":feature:settings-core:api",
    ":feature:settings-core:impl",
    ":feature:settings-theme:api",
    ":feature:settings-theme:impl",
    ":feature:settings-notification:api",
    ":feature:settings-notification:impl",
    ":feature:settings-alarm:api",
    ":feature:settings-alarm:impl",
    ":feature:settings-device:api",
    ":feature:settings-device:impl",
    ":feature:settings-admin:api",
    ":feature:settings-admin:impl",
    ":feature:settings-lab:api",
    ":feature:settings-lab:impl",
    ":feature:ui-scheme:api",
    ":feature:ui-scheme:impl",
    ":feature:ui-setting:api",
    ":feature:ui-setting:impl",
    ":feature:ui-web:api",
    ":feature:ui-web:impl",
    ":shared",
    ":widget",
)
