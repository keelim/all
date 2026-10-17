# :feature:app-function

## Focus

App Functions are grouped by capability in `json/` and `health/`; keep their tests aligned with the public function behavior.

## Boundaries

- Treat App Function names, parameters, and result models as external contracts.
- Document all AppFunctions and `@AppFunctionSerializable` classes with structured KDoc tags (`@param`, `@return`, `@property` or inline property docs) and `isDescribedByKDoc = true` so KSP generates complete XML schemas (`app_functions_v2.xml`) for on-device AI discovery.
- Delegate data access to repositories; keep function adapters thin and validation explicit.
