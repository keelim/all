# shared Module

## OVERVIEW
Kotlin Multiplatform (KMP) module centralizing data models, Room databases, and platform abstractions for the entire monorepo. It serves as the primary data layer for Android, Desktop, and Apple (iOS) applications.

## STRUCTURE
- `src/commonMain`: Core logic, Room databases, DAOs, and entities.
- `src/androidMain`: Android-specific Room implementation and platform helpers.
- `src/desktopMain`: Desktop (JVM) implementation and Compose Multiplatform entry point.
- `src/appleMain`: iOS/macOS implementation with Objective-C compatibility.
- `src/commonMain/kotlin/.../data/database`: Multi-database architecture.

## WHERE TO LOOK
- **Database definitions**: `src/commonMain/.../data/database/AppDatabase.kt`
- **DAOs**: `src/commonMain/.../data/database/dao/`
- **Entities**: `src/commonMain/.../data/database/model/`
- **DataStore**: `src/commonMain/.../data/AppDataStore.kt`
- **Platform abstractions**: `src/commonMain/.../Platform.kt`

## MULTIPLATFORM
- **Room KMP**: Uses `expect`/`actual` constructors with `@ConstructedBy` for cross-platform persistence.
- **Desktop**: Leverages Compose Multiplatform for shared UI elements like `DesktopWindow`.
- **Apple**: Uses `@ObjCName` on entities for seamless Swift/Objective-C integration.

## DATABASE MODELS
- Shared entities across all apps: `History`, `DeepLinkEntity`, `NandaEntity`, `AlarmEntity`, `StudySession`, etc.
- Schema versioning managed via Room's `AutoMigration`.

## CONVENTIONS
- All shared entities MUST use `@ObjCName` for iOS compatibility.
- Prefer `expect`/`actual` for platform-specific capabilities (e.g., `Platform`, `AppSupported`).
- Use Kotlinx Serialization for data models that require persistence or network transfer.

## ANTI-PATTERNS
- Avoid platform-specific libraries in `commonMain`.
- Do not duplicate data models in app modules; use `shared` as the source of truth.

## NOTES
- Target JVM 17, Min SDK 26 (Android).
- Uses KSP for cross-platform Room code generation.
