# Local Android work

Prepare a JDK supported by the checked-in Gradle wrapper, Android SDK, and the existing local SDK/provider configuration. The JVM compilation target is 17; it is not a command to replace the installed Gradle runtime. Preserve local.properties, local.defaults.properties, Firebase files, and signing material without printing their values.

Inspect settings.gradle.kts and the changed modules' build.gradle.kts files to determine ownership and available task types. For changed Android modules with the permitted task, invoke from this repository root, combining targets:

```sh
./gradlew :app-arducon:testDebugUnitTest :app-cnubus:testDebugUnitTest
```

The example applies only when both modules changed. If a changed unit lacks that task, report unavailable verification. Do not substitute assemble, lint, root test, connected checks, diff-check, or native/platform builds without an explicit request.

Dependency changes belong in gradle/libs.versions.toml and the appropriate convention/module configuration. Do not run an installation or regenerate locks merely to validate prose. Build/release workflows are per app; dispatching releases or changing signing/provider credentials is separate authorized work. The Rust crate, TypeSpec package, and Xcode shell require their own prerequisites and explicit verification scope rather than an assumed Android check.
