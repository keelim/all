# app-arducon ViewModel Coverage Design

## Goal
Raise meaningful unit-test coverage across **all app-arducon ViewModels** by focusing on deterministic ViewModel behavior rather than chasing whole-module 90% immediately.

## Scope
- Existing tests stay in scope: `CreateDeepLinkViewModel`, `SearchViewModel`
- New/expanded coverage targets:
  - `UrlShortenerViewModel`
  - `Base64ViewModel`
  - `MainViewModel`
  - `PlaygroundViewModel`
  - `JsonFormatterViewModel`
  - `OgTagPreviewViewModel`
  - `StatsViewModel`
  - `SaastatusViewModel`
  - `SaastatusSearchViewModel`

## Recommended execution model
Use a 3-worker OMX team with TDD:
1. Worker 1: `UrlShortenerViewModel`, `Base64ViewModel`
2. Worker 2: `MainViewModel`, `PlaygroundViewModel`, `JsonFormatterViewModel`, `OgTagPreviewViewModel`
3. Worker 3: `StatsViewModel`, `SaastatusViewModel`, `SaastatusSearchViewModel`, plus audit existing `CreateDeepLinkViewModel` and `SearchViewModel` tests and run final coverage verification.

## Test strategy
- Write failing tests first for each ViewModel behavior.
- Prefer deterministic state-transition and validation tests.
- Mock repositories/interfaces already used by the app.
- Avoid UI/instrumentation scope unless unit coverage is impossible.

## Verification
- Targeted tests while iterating
- `./gradlew :app-arducon:testDebugUnitTest`
- `./gradlew :app-arducon:jacocoTestReport`
- `./gradlew :app-arducon:assembleDebug`
