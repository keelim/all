# :core:designsystem

## Focus

Canonical Android Compose tokens and primitives are under `theme/` and `component/`; begin with `theme/Theme.kt`.

## Boundaries

- Add reusable tokens or primitives here before adding screen-local style values.
- Update the root `DESIGN.md` in the same change as any token, theme, or primitive API change; the Kotlin sources stay authoritative.
- Keep public design tokens stable; use `:core:component` for larger compatibility and interactive widgets (`VPrism`, `TextStream`, `KeelimNavDisplay`).
