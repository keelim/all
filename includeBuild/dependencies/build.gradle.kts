plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

// To make it available as direct dependency
group = "com.keelim.dependencies"
version = "SNAPSHOT"

repositories {
    jcenter()
}

gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "com.keelim.dependencies.DependenciesPlugin"
    }
}