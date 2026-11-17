# :feature:ui-a2ui

A reusable, experimental Android Compose renderer for a read-only A2UI 1.0 profile.

- Keep the catalog allowlist and parser strict. Never evaluate code, load remote catalogs,
  open URLs, or add network/media/function execution implicitly.
- Validate graphs and resource budgets before publishing a snapshot. Rejected messages
  must preserve prior state and must not echo payload data into errors or logs.
- Keep rendering on canonical core:designsystem tokens. Hosts own transport, scrolling,
  lifecycle, and application navigation. Do not add an exported activity for the sample.
- Update README support limits, catalog schema, and focused tests when extending the profile.
- Verification target: `:feature:ui-a2ui:testDebugUnitTest`.
