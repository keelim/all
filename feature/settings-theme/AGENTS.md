# :feature:settings-theme

## Focus

Theme selection is implemented by `screen/theme/ThemeScreen.kt`, `ThemeViewModel.kt`, and `navigation/SettingsThemeNavigation.kt`.

## Boundaries

- Keep theme persistence in the ViewModel or repository boundary, not in Composables.
- Retain animated selection feedback and existing unit/UI test coverage when altering theme behavior.
