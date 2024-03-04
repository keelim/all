pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "all"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    //":app-benchmark",
    ":app-cnubus",
    ":app-my-grade",
    ":app-nanda",
    ":app-comssa",
    ":catalog",
    ":core:common",
    ":core:common-android",
    ":core:compose-core",
    ":core:data",
    ":core:domain",
    ":core:model",
    ":core:testing",
    // ":core:widget-glance",
    ":features:ui-labs",
    ":features:ui-setting",
    ":shared"
)
