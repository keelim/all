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
    ":app-benchmark",
    ":app-cnubus",
    ":app-comssa",
    ":app-my-grade",
    ":app-nanda",
    // ":app-yr",
    ":core:common",
    ":core:common-android",
    ":data",
    ":domain",
    ":compose:compose-core",
    ":compose:widget-glance",
    ":features:ui-setting",
    ":features:ui-map",
    ":features:ui-labs"
)
