# :feature:app-function

The feature contract lives in `api/`. Implementation sources and existing tests live in `impl/`.

## Focus

App Functions are grouped by capability in `json/` and `health/`; keep their tests aligned with the public function behavior.

## Boundaries

- Treat App Function names, parameters, and result models as external contracts.
- Document all AppFunctions and `@AppFunctionSerializable` classes with structured KDoc tags (`@param`, `@return`, `@property` or inline property docs) and `isDescribedByKDoc = true` so KSP generates complete per-service XML schemas (`json_formatter_app_functions.xml` and `health_app_functions.xml`) for on-device AI discovery.
- Delegate data access to repositories; keep function adapters thin and validation explicit.

## Stable API and validation

- Keep the abstract service class names unchanged: they determine existing external function IDs. Use `@AppFunctionServiceEntryPoint` and `@AppFunctionDeclaration`; register the generated concrete services with the platform binding permission.
- The pinned Robolectric 4.16.1 supports API 36 on Java 21. This module runs its service tests on that runner while Android compilation retains the repository Java 17 target. Keep the JUnit Vintage engine so the API 36 service tests are discovered alongside Kotest.
- `.github/scripts/verify-common-build.py` checks generated schemas, actual test discovery and consumer APK assets/service declarations. Public offline CI fixtures must never contain production configuration.
