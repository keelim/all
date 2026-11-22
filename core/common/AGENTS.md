# :core:common

## Focus

Shared extensions, models, and platform contracts live in `src/commonMain`.
APIs that use Java time or locale-sensitive JVM formatting live in `src/jvmAndAndroidMain`.
Android infrastructure, Hilt bindings, and startup initializers live in `src/androidMain`.
Android host-side unit tests live in `src/androidHostTest`.

## Boundaries

- Keep Android framework dependencies in the Android source sets.
- Put shared user-facing formatting in the existing extension files so callers use one rule.
- New initializers must be safe during application startup and declare their dependencies explicitly.
- Platform file exports use `PlatformFileProvider` with authority `${applicationId}.keelim-platform-files` to avoid manifest provider authority collisions.
