# core:navigation

## OVERVIEW
Shared route model module for all apps. This module defines route contracts only and should remain platform-free.

## ROUTE EXTENSION RULES
- Every route must implement `AppRoute` through a sealed route group (for example `MyGradeRoute`, `FeatureRoute`).
- Every route type must be annotated with `@Serializable`.
- Use `data object` for routes with no arguments.
- Use `data class` for routes with arguments.
- Keep cross-app routes in dedicated groups (for example `FeatureRoute`) instead of app-specific groups.
- Navigation registration APIs must be typed as `EntryProviderScope<AppRoute>` and `SnapshotStateList<AppRoute>`.
- Do not use Any-typed entry providers or Any-typed back stack lists in navigation code.

## CHECKLIST FOR NEW ROUTES
1. Add the route in `/Users/keelim/Desktop/all/core/navigation/src/main/java/com/keelim/core/navigation/RouteModel.kt`.
2. Register a matching `entry<...>` in the host or feature navigation entry file.
3. Wire navigation actions (`backStack.add(...)`, back handling) from UI callbacks.
4. This module has no `testDebugUnitTest` task; report validation as unavailable and do not substitute another task unless explicitly requested.

## SETTINGS DESTINATION EXTENSION EXAMPLE
```kotlin
sealed interface FeatureRoute : AppRoute {
    sealed interface SettingsDestination : FeatureRoute

    @Serializable
    data object Settings : SettingsDestination

    @Serializable
    data object Theme : SettingsDestination

    @Serializable
    data object NewSettingsScreen : SettingsDestination
}
```
