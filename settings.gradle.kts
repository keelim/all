enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
    }
}
rootProject.name = "nandaDiagnosis"
include(
    ":app",
    ":data",
    ":common",
    ":compose",
    ":domain",
    ":features:ui-category",
    ":features:player",
    ":features:ui-setting"
)

include(":features:dynamic_vitamin")
