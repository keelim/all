# :core:device

## Focus

Platform-independent models live in `src/commonMain/kotlin/`. Android device integrations and their manifest live in `src/androidMain/`.

## Boundaries

- Keep Android permissions, broadcasts, Hilt bindings, and SDK dependencies in `androidMain`.
- Match manifest declarations to receiver and integration behavior.
