plugins {
    id("keelim.android.test")
}

android {
    namespace = "com.keelim.benchmark"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("my-grade-benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
        create("comssa-benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
        create("nanda-benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
        create("cnubus-benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }
    // todo 수정 할 것
    targetProjectPath = ":app-my-grade"
    // targetProjectPath = ":app-comssa"
    // targetProjectPath = ":app-nanda"
    // targetProjectPath = ":app-cnubus"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation("androidx.benchmark:benchmark-macro-junit4:1.2.4")
    implementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.test.ext:junit:1.2.0")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
}

androidComponents {
    // beforeVariants(selector().all()) {
    //     it.enabled = (it.buildType == "my-grade-benchmark") ||
    //         (it.buildType == "comssa-benchmark") ||
    //         (it.buildType == "nanda-benchmark") ||
    //         (it.buildType == "cnubus-benchmark")
    // }
}
