# :feature:settings-notification

## Focus

Notification settings UI and state live in `screen/notification/`; navigation is `navigation/SettingsNotificationNavigation.kt`.

## Boundaries

- Keep notification history and preference work in the ViewModel/repository path.
- Preserve list state and navigation behavior with the existing ViewModel tests.
