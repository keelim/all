# :feature:settings-core

## Focus

The settings hub is in `screen/settings/`; onboarding is in `screen/welcome/`; entry wiring is `navigation/SettingsCoreNavigation.kt`.

## Boundaries

- Keep the hub responsible for settings navigation and shared state, not child-feature UI.
- Preserve press-feedback and ViewModel tests when changing settings interactions.
