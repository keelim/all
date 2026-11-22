# :feature:settings-admin

The feature contract lives in `api/`. Implementation sources and existing tests live in `impl/`.

## Focus

Admin settings are in `screen/admin/`; navigation is `navigation/SettingsAdminNavigation.kt`.

## Boundaries

- Keep admin actions explicit, state-driven, and separated from ordinary user settings.
- Preserve ViewModel test coverage for all admin action state changes.
