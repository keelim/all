# all Project

Multi-app Android monorepo. Jetpack Compose, Hilt, Room/DataStore, MVVM + UDF, convention-plugin Gradle. CLAUDE.md symlinks here.

## STRUCTURE

```
all/
├── app-*/              # 6 apps, all registered: arducon, cnubus, comssa, my-grade, mysenior, nanda
├── core/               # shared: common, common-android, component, data, data-api, database,
│                       #   datastore-proto, designsystem, device-android, domain, model,
│                       #   navigation, network, resource, testing
├── feature/            # app-function, settings-{core,theme,notification,alarm,device,admin,lab},
│                       #   ui-scheme, ui-setting, ui-web
├── shared/             # KMP shared code (database models/DAOs, desktop window)
├── composeApp/         # KMP app module
├── widget/, benchmarks/, catalog/
├── build-logic/        # convention plugins
└── .github/workflows/  # per-app CI + shared workflows
```

`settings.gradle.kts` is the source of truth for registered modules — verify there before claiming what builds.

## WHERE TO LOOK

| Task | Location |
|------|----------|
| Shared UI components | `core/component/` (Material 3, higher-level widgets) |
| Design tokens/theme | `core/designsystem/` (canonical Android Compose surface) |
| Data layer | `core/data/` (impls), `core/data-api/` (interfaces) |
| Database | `core/database/` (Room), `shared/` (KMP models/DAOs) |
| Navigation | `core/navigation/` |
| Settings screens | `feature/ui-setting/`, `feature/settings-*/` |
| Build conventions | `build-logic/convention/`, `gradle/libs.versions.toml` |

Apps: **my-grade** (grade calc, timer, study analytics), **arducon** (deeplink tester, QR, JSON formatter), **nanda** (NANDA diagnosis, food/exercise/water tracking), **comssa** (financial calculators, economic calendar), **cnubus** (CNU bus + Google Maps), **mysenior** (minimal senior app).

## CONVENTIONS

### UI (CRITICAL)
- `Text` composables MUST set `style` and `color` explicitly (`MaterialTheme.typography.*` / `MaterialTheme.colorScheme.*`).
- User actions MUST be animation-friendly: `LazyColumn` + `animateItem`, `AnimatedVisibility` for visibility changes.

### Design System Migration
- Android-only scope — never edit Web surfaces or sibling Web repos.
- `core:designsystem` = canonical tokens/themes/primitives; `core:component` = compatibility wrapper / higher-level widgets on top of it.
- Preserve domain behavior, business rules, and calculation results; layout may reorganize only for design-system fit.
- Uncommitted changes in `core/designsystem` or `core/component` are user-owned — integrate, don't revert.
- Gate: run `:<app>:assembleDebug` per affected app; report intentionally remaining direct `MaterialTheme`/hardcoded color/spacing usages.

### Formatting (Date/Currency/Number)
- User-facing date/currency/number strings MUST go through shared formatters.
- UI dates: `yyyy.MM.dd`, `yyyy.MM.dd HH:mm`, `HH:mm` (24h), system timezone.
- Numbers: `NumberFormat.getNumberInstance(Locale.getDefault())` via shared extensions; currency locale-based, pass explicit `Currency` when domain data has a code.
- No hardcoded units (`"원"`) in screen strings; use resources or formatter output.
- Persistence/API stays ISO/epoch (`yyyy-MM-dd`, ISO-8601, epoch).
- Forbidden: UI date composition via `String.format`; UI money via `DecimalFormat("#,###")`. Zero-padding uses a shared helper, never inline `%02d`.
- Exception: file names, logs, debug-only text.

### Build & Code Style
- Verify with `./gradlew :<module>:assembleDebug` after changes.
- All dependency versions in `gradle/libs.versions.toml`; apply convention plugins (`keelim.android.application`, `keelim.android.library.compose`, …) — don't bypass them.
- Google Android Kotlin Style Guide; no hardcoded strings (use resources); no deprecated APIs; Hilt for DI; ViewModels expose StateFlow/Flow.
- JVM 17 target, `-Xcontext-parameters` enabled, Parcelize auto-applied.

## ANTI-PATTERNS

No `@Suppress` without justification · no network/DB on main thread · no global mutable state · no SQL string concatenation (parameterize) · don't bypass convention plugins.

## COMMANDS

```bash
./gradlew :app-my-grade:assembleDebug   # per-app build
./gradlew assembleDebug                 # all apps
./gradlew test
./gradlew dependencyGuard
```

CI: per-app workflows in `.github/workflows/` (`app_*.yml`) plus `ci.yml`, `app_deploy.yml`, `release*.yml`.

## GOTCHAS

- Target API 36 (Android 16), min SDK 26, desugaring enabled.
- `shared/` desktop window code only used in compose-desktop contexts.
- Registered app modules build/deploy independently via their own workflows.

## DETAILED DOCS

Module internals: `app-my-grade/AGENTS.md`, `app-arducon/AGENTS.md`, `app-nanda/AGENTS.md`, `core/component/AGENTS.md`, `core/navigation/AGENTS.md`, `core/data/AGENTS.md`, `feature/ui-setting/AGENTS.md`, `shared/AGENTS.md`.

## CODEGRAPH SEARCH

From the `keelim-maestro` workspace root, use the root dispatcher (not a root aggregate graph):

```bash
bun run cg -- context all "<task>"
bun run cg -- query all <symbol>
bun run cg -- files all --max-depth 2
```

Starting inside this repo directly: check `/Users/keelim/Desktop/keelim-maestro` for the dispatcher and prefer child-targeted `--path` calls. Never initialize a workspace-root `.codegraph/` mixing child repos.
