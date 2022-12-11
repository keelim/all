pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "all"
include(
    ":app-cnubus",
    ":app-comssa",
    ":app-my-grade",
    ":app-nanda",
    ":app-yr",
    ":common",
    ":common-android",
    ":data",
    ":domain",
    ":compose",
    ":features:ui-setting",
    ":features:ui-map",
    ":features:ui-labs"
)
