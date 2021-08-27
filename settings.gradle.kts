dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
    }
}
rootProject.name = "cnubus"
include(
    ":app",
    ":common",
    ":domain",
    ":ui-map",
    ":data",
    ":compose",
)
arrayOf(
    ":ui-map"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}