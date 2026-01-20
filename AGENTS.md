# all Project

**Generated:** 2026-01-21 00:24:37 KST  
**Commit:** 78a33baf7  
**Branch:** develop

## OVERVIEW

Multi-app Android monorepo with 6 apps (grade calculator, deeplink tester, health tracker, bus info, finance tools, senior helper). Modern architecture: Jetpack Compose, Hilt DI, Room/DataStore, MVVM + UDF, multi-module Gradle with custom convention plugins.

## STRUCTURE

```
all/
├── app-*/              # 6 independent apps (my-grade, arducon, nanda, comssa, cnubus, mysenior)
├── core/               # 14 shared modules (component, data, database, model, etc.)
├── feature/            # 5 shared feature modules (ui-setting, ui-scheme, etc.)
├── shared/             # Kotlin Multiplatform shared code (89 files, database models/DAOs)
├── build-logic/        # Custom Gradle convention plugins (17 plugins)
└── .github/workflows/  # Per-app CI + shared workflows
```

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| Add new app | `app-*/` | Clone existing app structure, update settings.gradle.kts |
| Shared UI components | `core/component/` | 144 files, Compose Material 3 components |
| Data layer | `core/data/`, `core/data-api/` | Repository pattern, API definitions |
| Database | `core/database/`, `shared/` | Room (core), KMP database models (shared) |
| Theme/Design system | `core/designsystem/`, `core/component/` | Material 3 theming |
| Navigation | `core/navigation/` | Jetpack Navigation for Compose |
| Settings screen | `feature/ui-setting/` | 104 files, animated theme selector |
| Build conventions | `build-logic/convention/` | Custom Gradle plugins, version catalog |
| CI/CD | `.github/workflows/` | Per-app builds + shared workflows |

## MODULES

### Apps (app-*)
- **app-my-grade** (237 files): Grade calculator, timer, study analytics, vocabulary
- **app-arducon** (210 files): DeepLink tester, QR scanner, JSON formatter, device info
- **app-nanda** (206 files): NANDA diagnosis, food/exercise tracker, water intake
- **app-comssa** (28 files): Financial calculators, economic calendar, flashcards
- **app-cnubus** (12 files): CNU bus real-time info, Google Maps integration
- **app-mysenior** (4 files): Minimal app for seniors

### Core Modules (core/*)
- **component** (144 files): Shared Compose UI, custom components, theme utilities
- **data** (85 files): Repository implementations, data sources, FCM service
- **common-android** (61 files): Android utilities, extensions, helpers
- **model** (38 files): Data models shared across modules
- **database** (35 files): Room database, entities, DAOs, mappers
- **data-api** (25 files): Repository interfaces, API contracts
- **common** (24 files): Pure Kotlin utilities, no Android deps
- **network** (18 files): Retrofit, OkHttp, network layer
- **resource** (9 files): Shared resources
- **designsystem** (5 files): Material 3 design tokens
- **domain** (4 files): Use cases (if used)
- **navigation** (2 files): Navigation graphs
- **testing** (2 files): Test utilities

### Feature Modules (feature/*)
- **ui-setting** (104 files): Settings screens, theme selection with animations
- **ui-scheme** (14 files): Scheme-related UI
- **ui-web** (4 files): WebView screens
- **ui-labs** (0 files): Experimental features (empty)
- **ui-auth** (0 files): Auth UI (empty)

### Shared Module
- **shared/** (89 files): Kotlin Multiplatform code, database models/DAOs, desktop window (compose-multiplatform)

## CONVENTIONS

### UI (CRITICAL - from existing AGENTS.md)
- **Text Composables MUST specify `style` and `color` explicitly**
  ```kotlin
  Text(
      text = "Example",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
  )
  ```
- **All user actions MUST be animation-friendly**: Use `LazyColumn` with `animateItem`, `AnimatedVisibility` for visibility changes

### Build
- **Always verify builds**: Run `./gradlew :<module>:assembleDebug` after changes
- **Version catalog**: All dependencies in `gradle/libs.versions.toml`
- **Convention plugins**: Apply via `build-logic/convention/` (e.g., `keelim.android.application`, `keelim.android.library.compose`)

### Code Style
- **Google Android Kotlin Style Guide**: 4 spaces, camelCase, UpperCamelCase for files
- **No hardcoded strings**: Use string resources
- **No deprecated APIs**
- **Hilt for DI**: All apps use Hilt
- **MVVM + UDF**: ViewModels expose StateFlow/Flow

### Compiler Flags (from build-logic)
- Kotlin 1.9+, JVM 17 target
- Opt-ins: `@OptIn(ExperimentalCoroutinesApi::class)`, `@OptIn(ExperimentalMaterial3Api::class)`
- Context parameters enabled: `-Xcontext-parameters`
- Parcelize plugin auto-applied

## ANTI-PATTERNS

- **No `as any`, `@Suppress` without justification**
- **No network/DB on main thread**
- **No global mutable state**
- **No string concatenation in SQL** (use parameterized queries)
- **Don't bypass convention plugins** (use them for consistency)

## BUILD COMMANDS

```bash
# Build specific app
./gradlew :app-my-grade:assembleDebug
./gradlew :app-arducon:assembleDebug

# Build all apps
./gradlew assembleDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencyGuard

# Jacoco coverage (if configured)
./gradlew jacocoTestReport
```

## CI/CD

Per-app workflows in `.github/workflows/`:
- `app_my_grade.yml`, `app_arducon.yml`, `app_nanda.yml`, `app_comssa.yml`, `app_cnubus.yml`
- `ci.yml` - Main CI checks
- `app_deploy.yml` - Deployment
- `release.yml`, `release_tag.yml` - Release automation
- `gh_page.yml` - GitHub Pages
- `slack.yml` - Slack notifications

## GOTCHAS

- **Android 16 (API 36) target**: Uses latest APIs (e.g., `Notification.ProgressStyle` for live updates in app-comssa)
- **Multiplatform shared module**: Desktop window code in `shared/` - only used in compose-desktop contexts
- **6 apps, independent builds**: Each app has its own workflow, can be deployed separately
- **TODOs in code**: 11 TODOs found (mostly non-critical, see grep results)
- **Version catalog**: `libs.versions.toml` is the source of truth for all versions
- **Min SDK 26**: Supports Android 8.0+
- **Desugaring enabled**: Core library desugaring for Java 17 APIs on older Android

## DETAILED DOCS

For module-specific details, see:
- `app-my-grade/AGENTS.md` - Grade calculator internals
- `app-arducon/AGENTS.md` - DeepLink tester internals
- `app-nanda/AGENTS.md` - Health tracker internals
- `core/component/AGENTS.md` - Compose component library
- `feature/ui-setting/AGENTS.md` - Settings UI patterns
- `shared/AGENTS.md` - KMP shared code
- `core/data/AGENTS.md` - Data layer architecture
