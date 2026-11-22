# :feature:settings-core

The feature contract lives in `api/`. Implementation sources and existing tests live in `impl/`.

## Focus

The settings hub is in `screen/settings/`; onboarding is in `screen/welcome/`; entry wiring is `navigation/SettingsCoreNavigation.kt`.

## Boundaries

- Keep the hub responsible for settings navigation and shared state, not child-feature UI.
- Preserve press-feedback and ViewModel tests when changing settings interactions.
