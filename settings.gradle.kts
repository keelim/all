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
    ":feature:map",
    ":common",
    ":compose",
    ":domain",
    ":data"
)
