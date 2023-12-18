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
    ":core:testing",
    ":core:widget-glance",
    ":features:ui-labs",
    ":features:ui-setting",
)
