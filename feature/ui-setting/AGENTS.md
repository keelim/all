# Feature: UI-Setting

## OVERVIEW
Main settings module providing theme management, notification history, device information, and experimental features (Lab). Focused on high-quality animations and consistent Material 3 UI patterns.

## STRUCTURE
```
ui-setting/
├── screen/
│   ├── settings/      # Main list, carousel, category items
│   ├── theme/         # Animated theme selector + preview
│   ├── notification/  # History with sticky headers
│   ├── lab/           # AI-powered experimental features
│   └── device/        # Hardware/System info cards
├── di/                # Hilt modules
└── worker/            # Maintenance/Background tasks
```

## WHERE TO LOOK
- **Theme Animations**: `ThemeScreen.kt` uses `animateFloatAsState` and `animateColorAsState` for interactive feedback.
- **Settings Patterns**: `SettingsScreen.kt` defines `CategoryItem` for standardized rows with press animations.
- **Carousel UI**: `FamilyServiceCarousel` in `SettingsScreen.kt` using `HorizontalMultiBrowseCarousel`.
- **List Dynamics**: `NotificationScreen.kt` for sticky headers and `animateItem` placement animations.

## FEATURES
- **Animated Theme Selection**: Real-time preview with cross-fade and scale animations on selection cards.
- **AI Lab**: Interface for prompt queueing and response streaming (Gemini-ready).
- **Service Carousel**: Dynamic "Family Service" discovery via Firebase Remote Config.
- **Device Diagnostics**: Comprehensive hardware and OS metadata summary.

## CONVENTIONS
- **Interactive UI**: Always provide visual feedback using `animate*AsState` for clicks and selections.
- **State Flow**: Use `collectAsStateWithLifecycle` for all ViewModel state observations.
- **Preferences**: Read/Write theme and user settings via `UserStateStore` (KMP).

## ANTI-PATTERNS
- **Logic in Compose**: Business logic must reside in `ViewModel`, not in `Screen` or `Route`.
- **Static Lists**: Use `LazyColumn` even for short lists to ensure performance and animation support.
- **Direct DataStore**: Never access `DataStore` directly in UI; go through `UserStateStore` or a Repository.

## NOTES
- Min SDK 26 support.
- Heavily relies on `compose-util` for shared resources and spacing.
