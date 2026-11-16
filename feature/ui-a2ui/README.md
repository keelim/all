# A2UI feature

`:feature:ui-a2ui` is an experimental, reusable **read-only A2UI 1.0 profile** for
Android Compose. It is a small local implementation, not an official SDK or a
complete A2UI renderer. No application currently depends on it. The offline
`A2uiSample` composable and its Android Studio preview demonstrate integration
without a backend, credentials, or paid model calls.

## Official sources checked on 2026-11-16

- [Official repository](https://github.com/a2ui-project/a2ui) (the former
  `google/A2UI` URL redirects here) and [current v1.0 specification](https://a2ui.org/specification/v1.0-a2ui/).
- [Canonical wire and catalog schemas](https://a2ui.org/specification/v1.0/schemas/):
  version `1.0`, `createSurface`, incremental `updateComponents`, `deleteSurface`,
  flat component lists, and the fixed `root` child of the implicit Surface.
- [Official SDK matrix](https://a2ui.org/concepts/sdks/) and
  [Android Compose guide](https://developer.android.com/develop/ui/compose/agentic):
  AndroidX has official Kotlin/Compose support for **v0.9.1**, not v1.0.
- [Compose A2UI release notes](https://developer.android.com/jetpack/androidx/releases/a2ui-compose)
  list `androidx.a2ui.compose:compose-runtime` and `compose-ui` **1.0.0-alpha02**
  (2026-10-21). [Material3 A2UI release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3#material3-a2ui)
  list `androidx.compose.material3:material3-a2ui:1.0.0-alpha02` and document
  compileSdk **37.1** for this generation. The repository convention currently
  sets **37.0**. This feature keeps the existing SDK/dependency configuration;
  adopting the alpha renderer would need a separate compatibility migration.

The chosen scope makes the declarative rendering boundary available now with
existing dependencies and explicit limits. It does not claim Basic Catalog
compatibility or conformance to the complete v1.0 protocol. An agent must target
this custom catalog and restricted profile explicitly.

## Use

Add `implementation(projects.feature.uiA2ui)` to an Android Compose host.
Keep an `A2uiSession` in the host's ViewModel; invoke `accept` on a background
dispatcher for each **complete** JSON envelope in arrival order. The method is
synchronized and publishes immutable snapshots through `surfaces: StateFlow`.
The host owns message framing, cancellation, lifecycle collection and transport
size/rate limits. Never pass a token stream or an entire JSONL batch as one message.

```kotlin
val session = A2uiSession()
val result = withContext(Dispatchers.Default) { session.accept(message) }
// Rejected means the prior state is unchanged. Show a host-owned error/retry UI.

// Inside a composable using the application's KeelimDesignSystemTheme:
val surfaces by session.surfaces.collectAsStateWithLifecycle()
surfaces["example"]?.let { document ->
    A2uiSurface(document = document)
}
// The host supplies lifecycle-runtime-compose for collectAsStateWithLifecycle.
// Supply a bounded viewport/scroll container appropriate to the host's screen.
```

Or mount `A2uiSample()` inside `KeelimDesignSystemTheme` for an offline demo.
Sample and placeholder copy uses Android string resources; incoming Text is
plain text and its localization remains the producer's responsibility.

```json
{
  "version": "1.0",
  "createSurface": {
    "surfaceId": "example",
    "catalogId": "https://github.com/keelim/all/a2ui/read-only/v1",
    "components": [
      {"id": "root", "component": "Card", "child": "content"},
      {"id": "content", "component": "Column", "children": ["title", "body"]},
      {"id": "title", "component": "Text", "text": "Welcome"},
      {"id": "body", "component": "Text", "text": "Native UI from validated data"}
    ]
  }
}
```

Subsequent messages can replace a component by ID or delete the surface:

```json
{"version":"1.0","updateComponents":{"surfaceId":"example","components":[{"id":"title","component":"Text","text":"Updated"}]}}
{"version":"1.0","deleteSurface":{"surfaceId":"example"}}
```

## Supported boundary

The bundled [catalog.json](src/main/resources/catalog.json) is the producer-facing
schema contract. Its URI is an identifier; the client never downloads it.
Only `Text(text: string)`, `Column(children: ID[])`, `Row(children: ID[])`, and
`Card(child: ID)` are registered. They use `core:designsystem` colors, typography,
spacing and panels, with animated snapshot changes. No style/color code arrives
from the producer.

- Only version `1.0`; create requires the exact catalog ID. Components may arrive
  inline or later; missing root/children render a localized placeholder.
- Updates replace the complete definition for each supplied ID and preserve other
  IDs. Deletion removes the surface. Creation of an existing surface is rejected.
- One message is limited to 65,536 Kotlin characters (not bytes), JSON nesting to
  32, literal text to 4,096 characters, active surfaces to 4, components/children
  to 128, graph depth to 16 and expanded nodes per subtree to 256. Limits apply
  across incremental updates; cycles and excessive DAG expansion are rejected
  even in unreachable subtrees. Bound transport bytes before allocating strings.
- IDs deliberately use the narrower ASCII pattern `[A-Za-z_][A-Za-z0-9_-]{0,63}`.
- Unsupported properties/types/messages reject the entire envelope atomically.
  `Rejected` deliberately contains no payload text, exceptions, or private data.

**Unsupported:** v0.8/v0.9/v0.9.1, full Basic Catalog, data models and bindings,
functions/RPC, actions/buttons, form input, templates, Markdown/HTML, custom
component registration, accessibility overrides, per-component catalog mixing,
metadata, capability negotiation and structured protocol error transport. Even
otherwise valid optional fields such as `sendDataModel` are rejected in this
profile. Native text/layout semantics provide baseline accessibility; screen
reader, device layout and focus testing have not been performed. Do not advertise
this as a fully conformant renderer or feed arbitrary full-protocol streams to it.

No WebView, script evaluation, reflection, URL/intent opening, media loading,
network permission or external catalog resolution is present. Adding any such
capability requires an explicit host contract and new validation tests.

## Verification

Run only the new module's tasks (build and lint explicitly requested for this work):

```sh
./gradlew :feature:ui-a2ui:testDebugUnitTest :feature:ui-a2ui:lintDebug :feature:ui-a2ui:assembleDebug
```

The tests exercise sample escaping, progressive trees, lifecycle and immutable
snapshot updates, strict rejection with prior-state preservation, cycles,
message/text/depth/count budgets, DAG expansion, and catalog contents. See the PR
for actual execution results and environment limitations. Release workflows must
not be used for validation; several deploy to production Google Play.
