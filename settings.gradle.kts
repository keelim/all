dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}

include(
    ":app",
    ":data",
    ":common",
    ":compose",
    ":domain",
    ":ui-reference-search",
    ":ui-category"
)

arrayOf(
    ":ui-reference-search",
    ":ui-category"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}

