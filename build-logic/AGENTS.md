# build-logic

## OVERVIEW
Centralized build configuration using Gradle convention plugins. Encapsulates shared logic for Android, JVM, and Multiplatform modules to ensure consistency across the monorepo while reducing boilerplate in feature modules.

## CONVENTION PLUGINS
| Plugin ID | Purpose |
|-----------|---------|
| `keelim.android.application` | Base Android app config (SDK 36, versionCode, buildTypes, lint, baseline profile) |
| `keelim.android.application.firebase` | Integrates Firebase Performance and Crashlytics |
| `keelim.android.application.compose` | Enables Jetpack Compose for application modules |
| `keelim.android.application.jacoco` | Configures Jacoco code coverage for applications |
| `keelim.android.application.room` | Sets up Room DB with KSP, schema directory, and testing support |
| `keelim.android.library` | Base Android library configuration |
| `keelim.android.library.compose` | Enables Jetpack Compose for library modules |
| `keelim.android.library.jacoco` | Configures Jacoco code coverage for libraries |
| `keelim.jvm.library` | Standard JVM (non-Android) library configuration |
| `keelim.android.test` | Configuration for Android test-only modules |
| `keelim.android.hilt` | Sets up Hilt DI with KSP and Android dependencies |
| `keelim.multiplatform` | Kotlin Multiplatform support (Desktop, Android, Wasm targets) |

## WHERE TO LOOK
- `convention/src/main/kotlin/`: Implementation of all convention plugins.
- `convention/src/main/kotlin/com/keelim/builds/`: Shared helper functions for Compose, Jacoco, and Kotlin/Android config.
- `convention/build.gradle.kts`: Plugin registration and dependency management for build logic.
- `settings.gradle.kts`: Build logic root configuration.

## USAGE PATTERNS
- Apply plugins via ID in module `build.gradle.kts`: `plugins { id("keelim.android.library") }`.
- Shared logic extracted into `com.keelim.builds` package for reuse across multiple plugins.
- Access version catalog via `libs` extension within plugins for version consistency.

## CONVENTIONS
- All plugins must use `libs.versions.toml` via `libs` extension for dependency resolution.
- Opt-ins (e.g., `ExperimentalMaterial3Api`) are applied globally in `KotlinAndroid.kt`.
- Use KSP for annotation processing where possible (Room, Hilt) to improve build speed.
- `KeelimAndroidApplicationPlugin` automatically applies quality tools like `cache-fix` and `dependency-guard`.

## ANTI-PATTERNS
- Hardcoding versions or dependency strings instead of using the version catalog.
- Manually configuring SDK versions (`compileSdk`, `targetSdk`) in module build files.
- Bypassing convention plugins for shared settings like Kotlin compiler flags or lint rules.

## NOTES
- Uses `TYPESAFE_PROJECT_ACCESSORS` for easier module navigation.
- Multiplatform plugin supports `jvm("desktop")`, `androidTarget()`, and `wasmJs`.
- Room plugin enforces schema export to `$projectDir/schemas` for auto-migrations.
