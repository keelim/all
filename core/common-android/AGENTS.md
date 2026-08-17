# :core:common-android

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Android shared infrastructure is organized by `initialize/`, `platform/`, `extensions/`, and `BaseApplication.kt`.

## Boundaries

- Keep Android lifecycle and framework integration here; keep domain rules in platform-neutral modules.
- New initializers must be safe during application startup and declare their dependencies explicitly.
