# App and shared-module contracts

The repository supplies applications and reusable modules, not one unified public HTTP service. Provider APIs keep their own authentication and error models.

| Boundary | Input | Output and failure |
| --- | --- | --- |
| Typed app navigation | Serializable AppRoute data object/class | Matching host entry and back-stack behavior; invalid/incomplete external intent produces a visible invalid state |
| Repository interfaces | Typed query/update arguments from ViewModels | Domain models/Flow/Result-style outcomes; features do not receive DAO ownership or repository-owned UI flags |
| App Functions | Declared function names, parameters, result models | Typed capability results; validation remains at adapters and persistence is delegated |
| Shared formatting | Stored date/time/number plus locale/currency context | User-facing localized text; persistence/API values remain ISO/epoch |
| Settings protobuf | Existing field numbers and compatible additions | Decodable settings across versions; removed identifiers are reserved rather than reused |
| Room/KMP models | Versioned entities and mappings | Persisted records and platform implementations; schema changes must preserve owning-store compatibility |
| Widget integration | Receiver declarations, metadata, asynchronous data | Glance updates; capability/permission failure must not block the main thread |

External deep links and WebView URLs are untrusted input. Route and App Function names/parameter types are compatibility surfaces: update callers when changing them. No universal account role, network status envelope, or cross-app data interchange format is promised.
