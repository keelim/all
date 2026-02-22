# Network Client Lifecycle Standard

## Purpose

Standardize network client ownership and responsibilities across the monorepo.

- Ktor: business network traffic (REST, cache fetch, link inspector, WebSocket).
- OkHttp: low-level call factory and image pipeline integration (Coil).
- Client creation: only in DI modules under `core/network`.

## Ownership and lifecycle

- `HttpClient` and `OkHttpClient` are application singletons from Hilt `SingletonComponent`.
- Repositories and ViewModels must never call `close()` on injected network clients.
- Repositories and ViewModels must never wrap injected `HttpClient` in `use {}`.
- WebSocket disconnect must close the active session, not the shared client instance.

## Allowed clients by layer

- `core/network`: defines and configures all network clients.
- `core/data`: consumes injected clients through qualifiers, no direct client construction.
- `app-*` and `feature/*`: no direct network stack access for business calls; use repository interfaces.
- `core/common-android`: image initialization uses the Hilt-provided Coil `ImageLoader`.

## Forbidden patterns

- `Jsoup.connect(...)` in app/viewmodel/repository code for network fetching.
- `URL(...).openConnection()` and direct `HttpURLConnection` in business data flows.
- `OkHttpClient.Builder()` outside `core/network` DI.
- `client.close()` or `client.use {}` on injected clients.

## New network API checklist

1. Define or reuse repository interface in `core/data-api`.
2. Implement in `core/data` with injected client qualifier from `core/network`.
3. Keep client creation/configuration inside `core/network` DI only.
4. Ensure retry/timeout/logging policy comes from shared network policy.
5. Verify no lifecycle violations with:
   - `rg -n "use<.*HttpClient|client\\.close\\(\\)" core/data app-*`
   - `rg -n "Jsoup\\.connect\\(|URL\\(.*\\)\\.openConnection\\(" core app-*`
   - `rg -n "OkHttpClient\\.Builder\\(" core app-*`
