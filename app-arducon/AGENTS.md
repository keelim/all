# app-arducon (DeepLink & Dev Tools)

## OVERVIEW
Developer utility app for testing DeepLinks, generating QR codes, and processing data. It acts as a testing ground for app schemes and a toolkit for common developer tasks (Base64, JSON, OG Tags).

## STRUCTURE
```
app-arducon/
├── src/main/java/com/keelim/arducon/
│   ├── ui/
│   │   ├── ArduconHost.kt      # Navigation host (NavDisplay)
│   │   └── screen/             # Feature implementations
│   │       ├── deeplink/       # DeepLink creation & testing
│   │       ├── qr/             # QR code scanning & generation
│   │       ├── json/           # JSON formatting utilities
│   │       ├── base64/         # Base64 encoding/decoding
│   │       ├── ogtag/          # OG Tag previewer
│   │       ├── saastatus/      # Service status monitoring
│   │       └── urlshortener/   # URL shortening service
│   └── data/                   # Playground data models
└── src/test/                   # ViewModel & Search logic tests
```

## WHERE TO LOOK
- **Entry Point**: `MainActivity.kt` sets up `KeelimTheme` and `ArduconApp`.
- **Navigation**: `ArduconHost.kt` defines the backstack and route mapping using `AppRoute`.
- **Core Logic**: `MainViewModel.kt` handles DeepLink history, category filtering, and QR generation.
- **DeepLink Testing**: `CreateDeepLinkScreen.kt` for constructing and launching schemes.

## FEATURES
- **DeepLink Management**: CRUD operations on deep links with bookmarking and usage tracking.
- **QR/Barcode**: Integrated scanner and generator for rapid DeepLink testing on physical devices.
- **Dev Utilities**: JSON Formatter with tree view, Base64 encoder, and URL shortener.
- **OG Tag Preview**: Meta tag extractor to verify how URLs appear when shared.
- **SaaS Status**: Monitoring tool for various cloud services and APIs.

## CONVENTIONS
- **Navigation**: Uses `NavDisplay` with `rememberViewModelStoreNavEntryDecorator` for scoped ViewModels.
- **DeepLink Execution**: Uses `SchemeNotificationManager` to trigger test links via system notifications.
- **QR Generation**: Logic moved to `core:common` but consumed via `generateQrBitmap` in ViewModels.
- **Test Hub Home**: Keep `MainScreen` as the real app testing hub. Add or reorganize tools through the UI-only catalog in `ui/screen/main/ArduconToolCatalog.kt`, and route actions through existing `MainRoute`/`ArduconHost` navigation callbacks.
- **Tool Hub Boundary**: Do not change `ArduconRepository`, Room schemas, or `DeepLink` models for home/tool-hub organization unless the feature explicitly needs persistence changes.
- **Resources**: Add new user-facing tool-hub strings in `core/resource/src/commonMain/composeResources/values/strings.xml`. This module uses `com.keelim.core.resource.*` plus `stringResource(...)` for Compose resources.
- **Verification**: For changed files in this module, run only `./gradlew :app-arducon:testDebugUnitTest` unless another task is explicitly requested.

## ANTI-PATTERNS
- **Intent Spawning**: Avoid `context.startActivity` inside screens; propagate events to `ArduconHost`.
- **Direct Repository Access**: Prefer using `ArduconRepository` interfaces rather than local data sources.

## NOTES
- **API 36 Support**: Uses latest Android features for live updates and notification styles.
- **Testing**: ViewModels are unit-tested in `src/test` using MockK and JUnit5.
