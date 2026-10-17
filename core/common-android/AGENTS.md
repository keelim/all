# :core:common-android

## Focus

Android shared infrastructure is organized by `initialize/`, `platform/`, `extensions/`, and `BaseApplication.kt`.

## Boundaries

- Keep Android lifecycle and framework integration here; keep domain rules in platform-neutral modules.
- New initializers must be safe during application startup and declare their dependencies explicitly.
- Platform file exports use `PlatformFileProvider` with authority `${applicationId}.keelim-platform-files` to avoid manifest provider authority collisions.
