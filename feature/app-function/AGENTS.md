# :feature:app-function

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

App Functions are grouped by capability in `json/` and `health/`; keep their tests aligned with the public function behavior.

## Boundaries

- Treat App Function names, parameters, and result models as external contracts.
- Delegate data access to repositories; keep function adapters thin and validation explicit.
