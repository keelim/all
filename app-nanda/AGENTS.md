# app-nanda

## OVERVIEW
Daily wellness and care tracking application ("Buddy Daily"). Provides private daily care weather check-ins, condition self-checks, weekly routine planning, trend insights, body measurement records, morning signal reminders, and platform privacy controls.

## STRUCTURE
```
app-nanda/src/main/java/com/keelim/nandadiagnosis/
├── di/
│   └── NandaPlatformModule.kt        # Hilt bindings for PrivacySettingsRepository, NotificationScheduler, TimeProvider
├── ui/
│   ├── screen/main/MainActivity.kt   # FragmentActivity host wiring KeelimAppShell, BiometricPrivacyAuthenticator, and WellnessRoute
│   └── theme/NandaTheme.kt           # App Material 3 theme
└── wellness/
    ├── WellnessViewModel.kt          # Core StateFlow coordinator for check-ins, care check-ins, routines, and measurements
    ├── ads/                          # Consent and adaptive AdMob banner controllers
    ├── morning/                      # MorningSignalViewModel and daily reminder scheduling
    └── ui/                           # Compose screens (WellnessScreen, Today/BuddyScreen, Plan/RoutineScreen, InsightsScreen, ToolsScreen, MorningSignalScreen)
```

## WHERE TO LOOK
- `ui/screen/main/MainActivity.kt`: App entry point connecting `KeelimAppShell`, `PrivacySettingsRepository`, and `WellnessRoute`.
- `wellness/ui/WellnessScreen.kt`: Main 4-tab scaffold (`TODAY`, `PLAN`, `INSIGHTS`, `TOOLS`), top-bar privacy mode toggle, and tab-level `BackHandler`.
- `wellness/ui/ToolsScreen.kt`: Health tools hub, sub-screen navigation (`MeasurementToolScreen`, `MorningSignalRoute`) with `BackHandler`, and `PrivacySettings` switches (`appLockEnabled`, `obscureRecentApps`, `blockScreenshots`).
- `wellness/morning/MorningSignalViewModel.kt`: Daily reminder state and notification scheduling logic.

## FEATURES
- **Daily Care & Condition Check-In (`Today`)**: One-tap care weather logging (`Comfortable`, `A little noticeable`, `Needs attention`) and detailed daily condition check-ins.
- **Weekly Routine Plan (`Plan`)**: Routine creation, weekly completion tracking, and duration validation.
- **Condition Trend & Insights (`Insights`)**: 7-day / 28-day condition trend charts and pattern recognition with privacy-mode masking.
- **Health & Privacy Tools (`Tools`)**: Body measurement records with ruler mode, morning signal reminder configuration, and persisted platform privacy controls (`FLAG_SECURE` screenshot blocking, recent-apps blur, biometric app lock).

## CONVENTIONS
- **State Management**: Use `collectAsStateWithLifecycle()` for all ViewModel state flows.
- **Dependency Injection**: Every screen route must use `hiltViewModel()`.
- **UI Components**: Follow Material 3 design system; use `trace()` for Composable performance monitoring.
- **Animations**: Implement smooth transitions using `animateFloatAsState` for progress indicators.

## TEST POLICY
- **No Compose UI tests**: Do not add or run Compose/instrumentation UI tests in `app-nanda`; `androidx.compose.ui.test`, `createComposeRule`, `createAndroidComposeRule`, and Compose test rules are intentionally excluded.
- **Default verification**: When files in this module change, run only `./gradlew :app-nanda:testDebugUnitTest`. Do not substitute build, lint, connected, repository-wide, or diff-check tasks unless explicitly requested.
- **Narrow exception**: A future non-Compose instrumentation integration test may be added only when explicitly requested; this policy does not disable that test type.
- **External Journey E2E**: Live emulator Journey verification is defined in `journeys/app-nanda-journey.xml` (evaluated via Android CLI `android run` / `android layout` without Compose test dependencies; report at `journeys/reports/app-nanda-report.json`).

## ANTI-PATTERNS
- **Main Thread Work**: Never perform database queries or heavy calculations in ViewModels without `viewModelScope`.
- **Hardcoding**: No hardcoded strings; use localized resources even for units (e.g., "ml", "kcal").
- **UI Logic**: Keep Composables lean; move complex conditional logic to ViewModels.

## NOTES
- Relies heavily on `shared` and `core:data` for database entities and repositories.
- Min SDK 26 is strictly enforced due to notification and background work requirements.
