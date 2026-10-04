# :core:common

## Focus

Platform-neutral primitives live under `extensions/`, `model/`, `platform/`, and `KeelimDispatchers.kt`.

## Boundaries

- Keep this module free of Android framework dependencies.
- Put shared user-facing formatting in the existing extension files so callers use one rule.
