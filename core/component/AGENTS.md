# core:component

## OVERVIEW
Central UI library providing a unified design system for all 6 apps. Built exclusively with **Jetpack Compose** and **Material 3 (Expressive)**, focusing on reusable, accessible, and animation-friendly components.

## STRUCTURE
```
core/component/src/*/com/keelim/composeutil/
├── component/          # Atom/Molecule UI components (appbar, button, card, etc.)
├── screen/             # Template layouts (setting, basic, tutorial)
├── ui/theme/           # Material 3 Theme & Typography definitions
└── util/               # Compose utilities (Modifier extensions, LazyList utils)
```

## WHERE TO LOOK
| Type | Location | Description |
|------|----------|-------------|
| **Theme** | `ui/theme/Theme.kt` | `KeelimTheme` and Expressive M3 setup |
| **Settings** | `screen/setting/` | Shared settings screens and theme selectors |
| **Templates** | `screen/` | Common layouts (Tutorial, Basic Screen) |
| **UI Atoms** | `component/` | 20+ categories of reusable UI widgets |
| **Modifiers** | `util/ModifierUtil.kt` | Custom shimmer, conditional, and touch modifiers |

## KEY COMPONENTS
- **KeelimTheme**: The root design system provider using `MaterialExpressiveTheme`.
- **SearchView**: Integrated search field with clear and search actions.
- **Modifier.shimmer()**: Standard shimmer effect for loading states.
- **Modifier.conditional()**: Utility to apply modifiers based on conditions.
- **Modifier.onTouchHeld()**: Custom touch interaction for continuous updates.
- **AppState**: Serializable state holder for complex navigation/UI state.

## CONVENTIONS
- **M3 First**: Always use `MaterialTheme.colorScheme` and `MaterialTheme.typography`.
- **Stateless Components**: Prefer stateless Composables; hoist state via callbacks.
- **Explicit Styling**: `Text` MUST specify `style` and `color` to ensure M3 consistency.
- **Animation Ready**: Use `AnimatedVisibility` and `animateItem` for all dynamic UI.
- **Preview Support**: Every component should have a `@Preview` with theme wrapper.

## ANTI-PATTERNS
- **No Hardcoding**: Never hardcode hex colors or pixel sizes (use tokens/DP).
- **No Direct ViewModels**: Reusable components in `core:component` should not depend on ViewModels.
- **No Platform Logic**: Keep components platform-agnostic unless in `android*` source sets.

## NOTES
- **Android 16+**: Components leverage the latest API targets for live updates and expressive UI.
- **KMP Support**: Most components are in `commonMain` for future Multiplatform expansion.
