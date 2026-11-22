# Compose application host

Own multiplatform composition and desktop/Wasm host packaging, not shared domain rules. Keep platform-only dependencies in their source sets. Generated Wasm output and distribution copies are build artifacts; declared size checks do not prove browser execution. Use only a changed-module testDebugUnitTest task if available; otherwise report verification unavailable without substituting desktop/Wasm builds.
