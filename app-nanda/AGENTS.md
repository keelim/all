# app-nanda

## OVERVIEW
Nursing support and health tracking application. Provides NANDA (North American Nursing Diagnosis Association) diagnosis search and comprehensive health trackers for daily physical activities and clinical support.

## STRUCTURE
```
app-nanda/src/main/java/com/keelim/nandadiagnosis/
├── ui/screen/         # Feature-based screen implementations
│   ├── diagnosis/     # NANDA diagnosis search & categories
│   ├── food/          # Food intake & calorie tracking
│   ├── water/         # Hydration tracker with animated ring
│   ├── medication/    # Medication schedules & reminders
│   └── nutrient/      # Nutrient tracking with specialized timers
├── notification/      # Medication alarm receivers & notifications
└── worker/            # Background data synchronization workers
```

## WHERE TO LOOK
- `ui/screen/diagnosis/`: Entry point for nursing diagnosis search.
- `ui/screen/water/WaterIntakeScreen.kt`: Implementation of the animated progress ring.
- `ui/screen/food/overview/FoodScreen.kt`: Daily calorie summary and food list.
- `notification/MedicationAlarmReceiver.kt`: Core logic for medication reminders.

## FEATURES
- **NANDA Diagnosis**: Searchable database of nursing diagnoses categorized by clinical domains.
- **Hydration Tracker**: Water intake logging with goal-based animated progress ring and weekly history charts.
- **Food & Exercise**: Calorie-conscious tracking of dietary intake and physical activities.
- **Medication Management**: Persistent reminder system for medication schedules with alarm integration.
- **Nutrient Timer**: Specialized tracking for nutrient intake durations.

## CONVENTIONS
- **State Management**: Use `collectAsStateWithLifecycle()` for all ViewModel state flows.
- **Dependency Injection**: Every screen route must use `hiltViewModel()`.
- **UI Components**: Follow Material 3 design system; use `trace()` for Composable performance monitoring.
- **Animations**: Implement smooth transitions using `animateFloatAsState` for progress indicators.

## TEST POLICY
- **No Compose UI tests**: Do not add or run Compose/instrumentation UI tests in `app-nanda`; `androidx.compose.ui.test`, `createComposeRule`, `createAndroidComposeRule`, and Compose test rules are intentionally excluded.
- **Default verification**: When files in this module change, run only `./gradlew :app-nanda:testDebugUnitTest`. Do not substitute build, lint, connected, repository-wide, or diff-check tasks unless explicitly requested.
- **Narrow exception**: A future non-Compose instrumentation integration test may be added only when explicitly requested; this policy does not disable that test type.

## ANTI-PATTERNS
- **Main Thread Work**: Never perform database queries or heavy calculations in ViewModels without `viewModelScope`.
- **Hardcoding**: No hardcoded strings; use localized resources even for units (e.g., "ml", "kcal").
- **UI Logic**: Keep Composables lean; move complex conditional logic to ViewModels.

## NOTES
- Relies heavily on `shared` and `core:data` for database entities and repositories.
- Min SDK 26 is strictly enforced due to notification and background work requirements.
