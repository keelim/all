# Module change rules

Keep app behavior in its owner, repository interfaces in `core:data-api`, implementations in `core:data`, and deterministic rules free of Android/UI/persistence dependencies. Coordinate interface/model changes with all persisted, transport, and UI consumers. Do not duplicate shared models in apps. Keep Android design-system work out of sibling web repositories.

Use convention plugins and the version catalog; do not hardcode versions or bypass shared SDK/compiler configuration. JVM target is 17 with context parameters and the existing Parcelize conventions. Keep Hilt DI and Flow/StateFlow outputs, Google Android Kotlin style, resource-based strings, and justified rather than blanket suppressions. No global mutable state or main-thread I/O.

Do not introduce deprecated APIs.

Compose Text sets style and color explicitly. Actions remain animation-friendly using animated visibility/item placement. Shared primitives use canonical design tokens; `core:component` is a compatibility/higher-widget layer. Preserve unrelated design-system changes and report remaining intentional hardcoded styles.

Route every UI date/currency/number string through shared formatters. Use locale NumberFormat, explicit Currency when available, and resource/formatter units. Do not use inline String.format dates, DecimalFormat money, or inline `%02d`; filenames, logs, and debug-only text are exceptions.

Every route implements AppRoute through a sealed group and is serializable; use data objects for no-argument routes and data classes for argument routes. Entry providers and back stacks are typed to AppRoute, never Any. Preserve protobuf field numbers and reserve removed numbers/names. Generated sources and Room schema changes follow their owning build/persistence boundary.

## Verification policy

For each module containing changed files, use only `./gradlew :<changed-module>:testDebugUnitTest`, combining available module tasks in one invocation. Do not run assembleDebug, root test, lint, connected tests, git diff --check, or unchanged-module tasks unless the user explicitly requests them. If that task does not exist, report verification unavailable; do not substitute another task. Repository documentation outside a registered module has no module unit-test target.

Do not add/run Compose UI tests in app-nanda. Its Compose test rules and instrumentation Compose dependencies are intentionally excluded; a non-Compose instrumentation test needs an explicit request. Preserve existing tests elsewhere without using them to bypass the default task policy. Use the owning source index and current settings.gradle.kts rather than stale module counts.
