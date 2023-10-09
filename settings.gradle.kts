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
    ":app-my-grade",
    ":app-nanda",
    // ":app-comssa",
    ":app-mysenior",
    ":core:common",
    ":core:common-android",
    ":core:data",
    ":core:domain",
    ":compose:compose-core",
    ":compose:widget-glance",
    ":features:ui-setting",
    ":features:ui-map",
    ":features:ui-labs"
)
