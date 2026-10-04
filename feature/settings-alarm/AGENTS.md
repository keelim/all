# :feature:settings-alarm

## Focus

Alarm settings are split between `AlarmScreen.kt`, `AlarmSection.kt`, `AlarmViewModel.kt`, and `SettingsAlarmNavigation.kt`.

## Boundaries

- Keep scheduling side effects behind the ViewModel and repository boundary.
- Preserve existing alarm-state test behavior when changing inputs or scheduling rules.
