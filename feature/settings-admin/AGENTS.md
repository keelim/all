# :feature:settings-admin

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Admin settings are in `screen/admin/`; navigation is `navigation/SettingsAdminNavigation.kt`.

## Boundaries

- Keep admin actions explicit, state-driven, and separated from ordinary user settings.
- Preserve ViewModel test coverage for all admin action state changes.
