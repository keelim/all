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
    ":app-my-grade",
    ":app-nanda",
    ":common",
    ":data",
    ":domain",
    ":compose",
    ":design",
    ":features:player",
    ":features:ui-category",
    ":features:ui-setting",
)
