# :feature:ui-scheme

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Scheme and deep-link activities are `SchemeActivity.kt`, `SchemeTestActivity.kt`, `IdActivity.kt`, and `IdDetailActivity.kt`; notification handling is in `notification/`.

## Boundaries

- Keep intent parsing and deep-link validation at the activity or navigation boundary.
- Surface invalid or incomplete scheme data as a user-visible state rather than assuming a valid intent.
