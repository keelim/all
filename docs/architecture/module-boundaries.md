# Module Boundary ADR

## Status
- Accepted for the March 7, 2026 module-structure granularization pass.

## Why
- `core/component` mixed reusable UI primitives with demo/sample/screen assets.
- `feature/ui-setting` is a settings monolith that should be split incrementally.
- `core/common-android`, `shared`, and `core/data` still need later extractions, but the dependency rules should be frozen before more source moves happen.

## Naming Convention
- `core:<capability>` for reusable horizontal platform or UI foundations.
- `feature:<domain>-<slice>` for user-facing feature slices with explicit ownership.
- `app-*` stays as product entrypoints only.
- `catalog` is the dedicated showcase/demo surface for non-production UI samples and previews.

## Allowed Dependency Directions
- `app-*` -> `feature:*`, `core:common-android`, selected `core:*` foundations, `shared` only where migration has not finished.
- `feature:*` -> `core:data-api`, `core:domain`, `core:model`, `core:navigation`, `core:resource`, selected reusable UI foundations.
- `feature:*` must not introduce new dependencies on `core:data` implementation modules.
- `core:data*` -> `core:data-api`, `core:model`, `core:network`, persistence modules.
- `core:network` owns client creation and policy. Other layers only consume injected clients.
- `catalog` may depend on production UI modules for demos/previews, but production modules must not depend on `catalog`.
- Android-only modules must not be introduced as dependencies of `commonMain` source sets.

## Forbidden Edges
- `feature:*` -> `core:network`
- `feature:*` -> `catalog`
- `app-*` -> demo/sample modules except for local developer-only tools
- `core:component` -> demo/sample/screen assets
- direct network-client construction outside `core:network`

## Target Split Map

### Active now
- `core:component` -> reusable UI primitives, theme resources, utilities, and navigation helpers only
- `catalog` -> demo/sample/screen assets extracted from `core:component`

### Planned next
- `feature:ui-setting` ->
  - `feature:settings-root`
  - `feature:settings-theme`
  - `feature:settings-notification`
  - `feature:settings-device`
  - `feature:settings-maintenance`
  - `feature:settings-admin`
  - `feature:settings-labs` only if explicitly promoted
- `core:common-android` ->
  - `core:app-runtime-android`
  - `core:observability-android`
  - `core:device-android`
- `shared` ->
  - `core:persistence-kmp`
  - `core:preferences-kmp`
  - platform bridge module as needed
- `core:data` ->
  - `core:data-impl`
  - `core:data-local`
  - `core:data-firebase`
  - bounded context modules only where reuse is proven

## Latent Module Decisions
- `catalog`: promote now as the demo/sample target module.
- `feature/ui-labs`: keep latent for now; only promote when the settings split gives it a product owner and dependency boundary.
- `feature/ui-auth`: keep latent for now; do not add to `settings.gradle.kts` until its role is explicit.

## Migration Notes
- Move the lowest-risk UI seams first.
- Keep package names stable during extraction to minimize consumer churn.
- Verify after each extraction that filesystem ownership matches `settings.gradle.kts`.
