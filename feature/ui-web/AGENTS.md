# :feature:ui-web

The feature contract lives in `api/`. Implementation sources and existing tests live in `impl/`.

## Focus

WebView integration lives in `WebView.kt`, `KeelimWebViewClient.kt`, and `WebEx.kt`.

## Boundaries

- Keep URL handling and WebView settings centralized in these integration points.
- Treat external content as untrusted; grant only the capabilities required by the screen.
