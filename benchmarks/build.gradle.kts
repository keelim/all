import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    alias(libs.plugins.keelim.jvm.library)
    alias(libs.plugins.kotlin.benchmark)
    alias(libs.plugins.kotlin.allopen)
}

dependencies {
    implementation(libs.kotlin.benchmark.runtime)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    configurations {
        named("main") {
            warmups = 20
            iterations = 10
            iterationTime = 3
            iterationTimeUnit = "s"
        }
    }
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = libs.versions.jmh.get()
        }
    }
}
