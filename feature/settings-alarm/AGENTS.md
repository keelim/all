# :feature:settings-alarm

The feature contract lives in `api/`. Implementation sources and existing tests live in `impl/`.

## Focus

Alarm settings are split between `AlarmScreen.kt`, `AlarmSection.kt`, `AlarmViewModel.kt`, and `SettingsAlarmNavigation.kt`.

## Boundaries

- Keep scheduling side effects behind the ViewModel and repository boundary.
- Preserve existing alarm-state test behavior when changing inputs or scheduling rules.
