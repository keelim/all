enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}
rootProject.name = "cnubus"
include(
    ":app",
    ":common",
    ":domain",
    ":data",
    ":compose",
    ":features:ui-map",
    ":features:ui-setting",
)
