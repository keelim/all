# core:data Module

## OVERVIEW
Implementation-heavy module providing the data layer for the entire monorepo. It implements repository interfaces from `core:data-api`, manages diverse data sources (Room, DataStore, Network, Firebase), and handles Firebase Cloud Messaging (FCM) services.

## STRUCTURE
```
core/data/
├── di/               # Hilt modules (RepositoryModule, DataModule) for dependency binding
├── message/          # AllFCMService for push notification receiving and processing
├── model/            # Data-layer specific models and mappers (Local vs Network)
├── paging/           # DBPagingSource for Jetpack Paging 3 integration
├── repository/       # Implementation of repositories (Medication, MarketNotification)
└── source/           # Aggregated repository and data source implementations
    ├── local/        # Local storage managers (DataStore, SharedPreferenceManager)
    └── [feature]/    # Feature-specific repositories (alarm, analytics, finance, etc.)
```

## WHERE TO LOOK
- **Add/Modify Repository**: Define/Update interface in `core:data-api`, implement in `core:data/source/` or `repository/`, and update `di/RepositoryModule.kt`.
- **Push Notifications**: Logic for handling incoming FCM messages is in `message/AllFCMService.kt`.
- **Preference Storage**: See `source/local/` for `DataStore` or `SharedPreferences` wrappers.

## REPOSITORY PATTERN
Repositories act as the single source of truth, coordinating between:
- **Local Persistence**: Room DAOs (defined in `shared` or `core:database`) and `DataStore`.
- **Remote Sources**: Retrofit services, Firebase Realtime Database, or mock network sources.
- **In-Memory Cache**: Performance optimization for frequently accessed data (e.g., `FinanceRssRepositoryImpl`).

## DATA SOURCES
- **Room**: Injected DAOs from other modules (mostly `shared` for KMP compatibility).
- **DataStore**: Modern preference storage (e.g., `MedicationRepositoryImpl` uses `preferencesDataStore`).
- **Firebase**: token management and realtime data sync via `FirebaseRepositoryImpl`.

## FCM
`AllFCMService` extends `FirebaseMessagingService`. It handles incoming payloads by injecting repositories (e.g., `AlarmRepository`) to persist notification data. It leverages `@ApplicationScope` to ensure background tasks survive UI lifecycle changes.

## CONVENTIONS
- **Dependency Injection**: Use `@Binds` in `RepositoryModule` to expose implementations to the rest of the app.
- **Concurrency**: Always specify `@Dispatcher(KeelimDispatchers.IO)` for repository constructors.
- **Reactive Data**: Prefer exposing data as `Flow<T>` to maintain Unidirectional Data Flow (UDF).
- **Mapping**: Keep `LocalTask` vs `NetworkTask` separate; use mappers to bridge the layers.

## ANTI-PATTERNS
- **No UI State**: Do not store UI state (like `isError` or `isLoading`) in repositories; use `Result<T>` or similar.
- **Direct DB Access**: Features must never access DAOs directly; always go through a Repository.
- **Context Leaks**: Only use `@ApplicationContext` when a `Context` is strictly required.

## NOTES
- This module bridges `core:data-api` (interfaces) and persistence modules (`core:database`, `shared`).
- Historical consolidation means many repositories are nested within the `source/` directory.
