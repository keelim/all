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
    ":app-cnubus-benchmark",
    ":app-comssa",
    ":app-comssa-benchmark",
    ":app-my-grade",
    ":app-my-grade-benchmark",
    ":app-nanda",
    ":app-nanda-benchmark",
    // ":app-yr",
    // ":app-yr-benchmark",
    ":common",
    ":common-android",
    ":data",
    ":domain",
    ":compose",
    ":features:ui-setting",
    ":features:ui-map",
    ":features:ui-labs"
)
