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
    ":common",
    ":data",
    ":domain",
    ":compose",
    ":features:player",
    ":features:ui-category",
    ":features:ui-setting",
    ":features:ui-map",
    ":features:labs"
)
