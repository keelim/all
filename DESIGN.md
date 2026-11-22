---
version: alpha
name: all
description: Kui design system for keelim Android and Compose Multiplatform apps, owned by core:designsystem.
colors:
  # Finance (light) — KuiThemeId.Finance, default when the system is light
  primary: "#0F172A"
  on-primary: "#F8FAFC"
  foreground: "#0A1628"
  muted-foreground: "#64748B"
  background: "#FFFFFF"
  surface: "#FFFFFF"
  surface-variant: "#F1F5F9"
  surface-soft: "#F5F6F7"
  surface-strong: "#ECECEE"
  border: "#E2E8F0"
  danger: "#DC2626"
  on-danger: "#FFFFFF"
  danger-soft: "#FEE2E2"
  on-danger-soft: "#7F1D1D"
  success: "#15803D"
  success-soft: "#DCFCE7"
  on-success-soft: "#052E16"
  warning: "#B45309"
  warning-soft: "#FEF9C3"
  on-warning-soft: "#431407"
  info: "#0369A1"
  info-soft: "#E0F2FE"
  on-info-soft: "#082F49"
  market-up: "#DC2626"
  market-up-soft: "#FBE9E9"
  market-down: "#1D4ED8"
  market-down-soft: "#E8EDFB"
  market-flat: "#64748B"
  market-flat-soft: "#F0F1F3"
  # Finance dark — KuiThemeId.FinanceDark, opt-in only
  finance-dark-primary: "#F8FAFC"
  finance-dark-on-primary: "#0F172A"
  finance-dark-background: "#020817"
  finance-dark-surface: "#030A19"
  finance-dark-surface-variant: "#121B2C"
  finance-dark-foreground: "#F8FAFC"
  finance-dark-muted: "#A5B4C8"
  finance-dark-border: "#1E293B"
  finance-dark-danger: "#FCA5A5"
  finance-dark-success: "#4ADE80"
  finance-dark-warning: "#FDE68A"
  finance-dark-info: "#93C5FD"
  finance-dark-market-up: "#F87171"
  finance-dark-market-down: "#60A5FA"
  finance-dark-market-flat: "#A5B4C8"
  # Admin black-and-white — KuiThemeId.AdminBw, default when the system is dark
  admin-primary: "#FFFFFF"
  admin-on-primary: "#0A0A0A"
  admin-background: "#0A0A0A"
  admin-surface: "#141414"
  admin-surface-variant: "#262626"
  admin-surface-soft: "#313131"
  admin-surface-strong: "#434343"
  admin-foreground: "#F8FAFC"
  admin-muted: "#94A3B8"
  admin-border: "#334155"
  admin-danger: "#FF6B6B"
  admin-danger-soft: "#450A0A"
  admin-success: "#4ADE80"
  admin-warning: "#FBBF24"
  admin-info: "#38BDF8"
  admin-market-up: "#FF6B6B"
  admin-market-down: "#6AA8FF"
  admin-market-flat: "#94A3B8"
typography:
  display-xl:
    fontFamily: System Sans
    fontSize: 72sp
    fontWeight: 700
    lineHeight: 80sp
    letterSpacing: 0em
  display-lg:
    fontFamily: System Sans
    fontSize: 52sp
    fontWeight: 700
    lineHeight: 60sp
    letterSpacing: 0em
  h1:
    fontFamily: System Sans
    fontSize: 32sp
    fontWeight: 700
    lineHeight: 40sp
    letterSpacing: 0em
  h2:
    fontFamily: System Sans
    fontSize: 24sp
    fontWeight: 600
    lineHeight: 32sp
    letterSpacing: 0em
  h3:
    fontFamily: System Sans
    fontSize: 20sp
    fontWeight: 600
    lineHeight: 28sp
    letterSpacing: 0em
  h4:
    fontFamily: System Sans
    fontSize: 16sp
    fontWeight: 600
    lineHeight: 24sp
    letterSpacing: 0em
  title:
    fontFamily: System Sans
    fontSize: 20sp
    fontWeight: 600
    lineHeight: 28sp
    letterSpacing: 0em
  title-sm:
    fontFamily: System Sans
    fontSize: 13sp
    fontWeight: 500
    lineHeight: 20sp
    letterSpacing: 0em
  body-lg:
    fontFamily: System Sans
    fontSize: 17sp
    fontWeight: 400
    lineHeight: 26sp
    letterSpacing: 0em
  body:
    fontFamily: System Sans
    fontSize: 15sp
    fontWeight: 400
    lineHeight: 22sp
    letterSpacing: 0em
  body-sm:
    fontFamily: System Sans
    fontSize: 14sp
    fontWeight: 400
    lineHeight: 20sp
    letterSpacing: 0em
  label:
    fontFamily: System Sans
    fontSize: 13sp
    fontWeight: 500
    lineHeight: 18sp
    letterSpacing: 0em
  caption:
    fontFamily: System Sans
    fontSize: 12sp
    fontWeight: 500
    lineHeight: 16sp
    letterSpacing: 0em
  label-sm:
    fontFamily: System Sans
    fontSize: 11sp
    fontWeight: 500
    lineHeight: 16sp
    letterSpacing: 0.5sp
  overline:
    fontFamily: System Sans
    fontSize: 11sp
    fontWeight: 500
    lineHeight: 16sp
    letterSpacing: 0.12em
  numeric:
    fontFamily: System Monospace
    fontSize: 15sp
    fontWeight: 400
    lineHeight: 22sp
    letterSpacing: 0em
  ticker:
    fontFamily: System Monospace
    fontSize: 28sp
    fontWeight: 600
    lineHeight: 36sp
    letterSpacing: 0em
  code:
    fontFamily: System Monospace
    fontSize: 13sp
    fontWeight: 400
    lineHeight: 20sp
    letterSpacing: 0.01em
rounded:
  xs: 8dp
  sm: 14dp
  md: 18dp
  lg: 24dp
  xl: 28dp
  full: 999dp
spacing:
  0: 0dp
  1: 4dp
  2: 8dp
  3: 12dp
  4: 16dp
  6: 24dp
  8: 32dp
  12: 48dp
  16: 64dp
  component-sm: 40dp
  component-md: 44dp
  component-lg: 48dp
  card-padding: 24dp
  section-gap: 16dp
elevation:
  none: 0dp
  card: 1dp
  soft: 3dp
  panel: 6dp
components:
  button:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    padding: 24dp
    height: 44dp
  button-secondary:
    textColor: "{colors.primary}"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    height: 44dp
  button-ghost:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.foreground}"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    height: 44dp
  button-danger:
    backgroundColor: "{colors.danger}"
    textColor: "{colors.on-danger}"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    height: 44dp
  text-field:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.foreground}"
    typography: "{typography.body}"
    rounded: "{rounded.md}"
    height: 48dp
  panel:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.lg}"
    padding: 24dp
  panel-soft:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.lg}"
    padding: 24dp
  card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.lg}"
    padding: 24dp
  badge:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.foreground}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: 8dp
  badge-success:
    backgroundColor: "{colors.success-soft}"
    textColor: "{colors.on-success-soft}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
  badge-danger:
    backgroundColor: "{colors.danger-soft}"
    textColor: "{colors.on-danger-soft}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
  dialog:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.lg}"
  sheet:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.xl}"
  metric-card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    typography: "{typography.numeric}"
    rounded: "{rounded.lg}"
  market-up-chip:
    backgroundColor: "{colors.market-up-soft}"
    textColor: "{colors.market-up}"
    typography: "{typography.numeric}"
    rounded: "{rounded.full}"
  market-down-chip:
    backgroundColor: "{colors.market-down-soft}"
    textColor: "{colors.market-down}"
    typography: "{typography.numeric}"
    rounded: "{rounded.full}"
---

# DESIGN.md

## Overview

`all` hosts six Android / Compose Multiplatform apps plus a widget and shared
libraries. The shared visual language is **Kui**, implemented in the
Android-only module `core:designsystem`. At the time of writing, `app-arducon`,
`app-my-grade`, `app-cnubus`, `app-comssa`, and `app-mysenior` consume it
(directly or through the `core:component` `KeelimTheme` facade). `app-nanda`
uses its own `NandaTheme` on plain `MaterialTheme`, and `widget` and the KMP
`composeApp` host do not use Kui. Moving an app onto Kui is a change owned by
that app. Kui is a quiet,
work-focused system: paper-white surfaces, deep ink text, slate borders,
compact status colors, and soft panel elevation. Screens should feel precise
and easy to scan during repeated daily use rather than atmospheric.

Kui is the Android counterpart of the web package `all-web-ui` (see its
`DESIGN.md` in the sibling repository). Both expose the same semantic roles —
finance light, admin black-and-white, status pairs, Korean market up/down/flat —
but each platform owns its own values. This file records the Android values as
they ship in Kotlin; it is not the source of truth for web.

## Ownership & Sources

The Kotlin sources are authoritative. When this file and the code disagree, the
code wins and this file must be updated in the same change.

| Concern | Owner | Source |
| --- | --- | --- |
| Theme root, `KuiTheme` accessor, `KuiThemeId` | `core:designsystem` | `theme/Theme.kt` |
| M3 color schemes | `core:designsystem` | `theme/Color.kt` |
| Extra color roles (soft surfaces, status, market) | `core:designsystem` | `theme/KuiColors.kt` |
| M3 type scale | `core:designsystem` | `theme/Type.kt` |
| Extra type roles (numeric, ticker, overline, code) | `core:designsystem` | `theme/KuiTypography.kt`, `theme/KuiFontFamily.kt` |
| Spacing, shapes, elevation | `core:designsystem` | `theme/KuiSpacing.kt`, `theme/KuiShapes.kt`, `theme/KuiElevation.kt` |
| `Kui*` primitives | `core:designsystem` | `component/` |
| `KeelimTheme` compatibility facade, `KeelimNavDisplay`, `VPrism`, `TextStream`, setting/tutorial templates | `core:component` | `ui/theme/Theme.kt`, `component/`, `screen/` |

Paths above are relative to
`core/designsystem/src/main/java/com/keelim/core/designsystem/` and
`core/component/src/*/com/keelim/composeutil/`. This split follows decision
`docs/tracking/decisions/0001-shared-ui-boundary.md`: canonical tokens and
primitives live in `core:designsystem`; `core:component` delegates to them and
hosts larger compatibility widgets. Do not create a second token system in
`core:component` or in an app module.

## Themes

`KeelimDesignSystemTheme(isDarkTheme, themeId, content)` wraps
`MaterialExpressiveTheme` and provides `LocalKuiColors`, `LocalKuiTypography`,
`LocalKuiSpacing`, and `LocalKuiElevation`. Read tokens through `KuiTheme`
(`colorScheme`, `typography`, `shapes`, `motionScheme`, `colors`, `type`,
`spacing`, `elevation`).

| `KuiThemeId` | M3 scheme | `KuiColors` | Selected by default |
| --- | --- | --- | --- |
| `Finance` | `financeLightColorScheme` | `financeKuiColors` | System light |
| `FinanceDark` | `financeDarkColorScheme` | `financeDarkKuiColors` | Never; pass explicitly |
| `AdminBw` | `adminBwDarkColorScheme` | `adminBwKuiColors` | System dark |

The YAML `colors` block uses unprefixed keys for `Finance`, `finance-dark-*` for
`FinanceDark`, and `admin-*` for `AdminBw`. Component entries reference the
`Finance` roles; the other themes swap values under the same roles.

Source comments can mislead: `financeDarkColorScheme` is labeled a
"backward-compat alias" and `financeDarkKuiColors` sits under an "admin-bw
(dark)" comment, but both are the complete `FinanceDark` theme. `LightColorScheme`
and `DarkColorScheme` are aliases for `Finance` and `AdminBw`.

## Colors

- **Primary (`#0F172A`)** — ink action and emphasis color. `onPrimary` is
  `#F8FAFC`.
- **Foreground (`#0A1628`)** — main readable text on light surfaces.
- **Muted (`#64748B`)** — captions, metadata, placeholders, secondary and
  tertiary roles. Finance maps M3 `secondary` and `tertiary` to this muted ink;
  there is no separate brand hue.
- **Background / Surface (`#FFFFFF`)** — paper-white layout and card surfaces.
- **Surface variant (`#F1F5F9`)** — M3 `surfaceVariant` and all `*Container`
  roles.
- **Surface soft / strong** — 4% and 8% ink washes (`#0A0F172A`, `#140F172A`)
  for ghost buttons, soft panels, and default badges.
- **Border (`#E2E8F0`)** — M3 `outline`; default rule and input stroke.
- **Status pairs** — danger (M3 `error`), success, warning, and info each have
  a solid color, an on-color, a soft container, and an on-container color in
  `KuiColors`.
- **Market up (`#DC2626`) / down (`#1D4ED8`) / flat (`#64748B`)** — Korean
  finance convention: 상승 is red, 하락 is blue. Soft variants are 10% alpha.
  Never swap these to the Western green/red convention.
- **Admin BW** — near-black `#0A0A0A` background, `#141414` surface, white
  primary, `#334155` borders, and dark-tuned status/market colors.

### Alpha roles

Some roles are translucent at runtime. The YAML stores only opaque six-digit
fallbacks, because design.md tools read eight-digit hex as `#RRGGBBAA`, while
Compose writes `Color(0xAARRGGBB)`. Each fallback is the runtime color blended
over that theme's surface. Code must use the `KuiColors` / `ColorScheme`
property, never the fallback hex.

| YAML key | Opaque fallback | Runtime Compose value | Property |
| --- | --- | --- | --- |
| `surface-soft` | `#F5F6F7` | `Color(0x0A0F172A)` (4% ink) | `KuiColors.surfaceSoft` |
| `surface-strong` | `#ECECEE` | `Color(0x140F172A)` (8% ink) | `KuiColors.surfaceStrong` |
| `market-up-soft` | `#FBE9E9` | `Color(0x1ADC2626)` (10%) | `KuiColors.upSoft` |
| `market-down-soft` | `#E8EDFB` | `Color(0x1A1D4ED8)` (10%) | `KuiColors.downSoft` |
| `market-flat-soft` | `#F0F1F3` | `Color(0x1A64748B)` (10%) | `KuiColors.flatSoft` |
| `finance-dark-surface-variant` | `#121B2C` | `Color(0x8C1E293B)` | `surfaceVariant` |
| `admin-surface-soft` | `#313131` | `Color(0x1FFFFFFF)` | `KuiColors.surfaceSoft` |
| `admin-surface-strong` | `#434343` | `Color(0x33FFFFFF)` | `KuiColors.surfaceStrong` |

`FinanceDark` `KuiColors.surfaceSoft`/`surfaceStrong` (`0x8C1E293B`,
`0xC71E293B`) and `AdminBw` soft market colors are also translucent and are
not listed in the YAML.

## Typography

`Type.kt` defines the M3 `Typography` as `KeelimTypography`; `KuiTypography`
adds four roles M3 lacks.

| YAML key | Compose slot |
| --- | --- |
| `display-xl`, `display-lg` | `displayLarge`, `displayMedium` |
| `h1` | `displaySmall` |
| `h2`, `h3`, `h4` | `headlineLarge`, `headlineMedium`, `headlineSmall` |
| `title` | `titleLarge` |
| `title-sm` | `titleMedium` (13sp/20sp) and `titleSmall` (13sp/18sp) |
| `body-lg`, `body`, `body-sm` | `bodyLarge`, `bodyMedium`, `bodySmall` |
| `label`, `caption`, `label-sm` | `labelLarge`, `labelMedium`, `labelSmall` |
| `numeric`, `ticker`, `overline`, `code` | `KuiTheme.type.*` |

**Fonts:** `KuiFontFamily.Sans` is `FontFamily.Default` and `Mono`/`Numeric`
are `FontFamily.Monospace`. Pretendard and JetBrains Mono are the intended
drop-in targets matching web, but they are **not bundled today**. Do not
describe screens as using them until font resources are added.

Use `numeric` for prices, rates, scores, and counters so digits align; use
`ticker` for a single large price-change figure. Reserve display sizes for real
hero numbers or splash screens, not panel headings. Letter spacing stays `0`
except `labelSmall` (0.5sp), `overline` (0.12em), and `code` (0.01em).
`Text` must set both `style` and `color` explicitly.

## Layout

Spacing scale (`KuiTheme.spacing`): 4, 8, 12, 16, 24, 32, 48, 64dp. There is no
20dp or 40dp step; use the nearest step instead of a literal. Semantic steps:
`componentSm/Md/Lg` = 40/44/48dp minimum control heights, `cardPadding` = 24dp,
`sectionGap` = 16dp.

Prefer dense but organized screens. Do not nest cards inside decorative cards;
repeated items may be cards, sections should be unframed. Keep touch targets at
least 44dp (`componentMd`) for primary controls, and keep layouts stable when
labels, loading text, or Korean/English mixed strings change length.

## Elevation & Depth

Borders first, shadows second. `KuiTheme.elevation`: `none` 0dp, `card` 1dp,
`soft` 3dp, `panel` 6dp. `KuiPanel` defaults to the `Elevated` variant; `Default`
and `Soft` variants use zero tonal elevation. Admin BW screens should stay flat
and rely on the stronger border and surface steps. No decorative gradients or
background blobs.

## Shapes

`KuiShapes` maps M3 shape slots:

| YAML | M3 slot | Radius | Typical use |
| --- | --- | --- | --- |
| `xs` | `extraSmall` | 8dp | Chips, tooltips |
| `sm` | `small` | 14dp | Buttons, segmented controls |
| `md` | `medium` | 18dp | Text fields, input groups, popup surfaces |
| `lg` | `large` | 24dp | Panels, cards, metric cards, `KuiDialogContent` |
| `xl` | `extraLarge` | 28dp | Modal bottom sheets (M3 default slot) |
| `full` | — | 999dp | Badges, avatars (literal `RoundedCornerShape(999.dp)`) |

## Components

All primitives live in `core:designsystem/component/` and are prefixed `Kui`.
They mirror the web primitive names so a screen can be described once.

| Group | Primitives |
| --- | --- |
| Actions | `KuiButton` (Primary, Secondary, Ghost, Danger × Sm, Md, Lg), `KuiFilledTonalButton`, `KuiOutlinedButton`, `KuiTextButton`, `KuiIconButton`, `KuiFilledIconButton`, `KuiFloatingActionButton` family, `KuiSingleChoiceSegmentedButtonRow` |
| Inputs | `KuiTextField`, `KuiFilledTextField`, `KuiOutlinedTextField`, `KuiTextarea`, `KuiCheckbox(Row)`, `KuiRadioGroup`/`KuiRadioRow`, `KuiSwitch(Row)`, `KuiSlider`, `KuiSelect`, `KuiExposedDropdownMenuBox`, `KuiCalendar`, `KuiDatePicker(Dialog)`, `KuiTimePicker`, `KuiLabel` |
| Surfaces | `KuiSurface`, `KuiPanel` (Default, Elevated, Soft), `KuiCard*`, `KuiElevatedCard`, `KuiOutlinedCard`, `KuiScrollArea`, `KuiDivider`/`KuiSeparator` |
| Data display | `KuiText`, `KuiBadge` (Default, Muted, Accent, Danger, Success, Warning, Info), `KuiAvatar`, `KuiTable*`, `KuiListItem`, `KuiDataNumber`, `KuiMetricCard`, `KuiMetricValue`, `KuiHorizontalMultiBrowseCarousel` |
| Feedback | `KuiAlert*`, `KuiToast*`, `KuiSnackbar(Host)`, `KuiProgress`, `KuiCircularProgressIndicator`, `KuiLoadingIndicator`, `KuiLoadingStatus`, `KuiSkeleton`, `KuiEmptyState` |
| Overlays | `KuiDialog*`, `KuiAlertDialog*`, `KuiBasicAlertDialog`, `KuiSheet*`, `KuiModalBottomSheet`, `KuiBottomSheetScaffold`, `KuiPopover*`, `KuiHoverCard*`, `KuiTooltip*`, `KuiDropdownMenu*`, `KuiSwipeToDismissBox` |
| Navigation | `KuiScaffold`, `KuiTopAppBar`, `KuiCenterAlignedTopAppBar`, `KuiShortNavigationBar(Item)`, `KuiModalNavigationDrawer`, `KuiModalDrawerSheet`, `KuiTabs*`, `KuiPrimaryTabRow`, `KuiPrimaryScrollableTabRow`, `KuiBreadcrumb`, `KuiAccordion*` |
| Onboarding | `KuiOnboardingTour`, `KuiOnboardingStepList`, `KuiOnboardingStepRow`, `KuiOnboardingHighlight` |

Notes for the common ones:

- **`KuiButton`** uses `shapes.small`, `labelLarge`, minimum height from
  `componentSm/Md/Lg`, and horizontal padding 16/24/24dp. Primary fills with
  `primary`; Secondary is a transparent outlined button with `primary` text;
  Ghost sits on `surfaceSoft`; Danger uses `error` / `onError`.
- **`KuiTextField`** uses `shapes.medium`, a `surfaceSoft` fill, an `outline`
  border that turns `primary` on focus, and a minimum height of `componentLg`
  (48dp).
- **`KuiPanel`** uses `shapes.large` and `cardPadding` when `padded = true`.
  **`KuiMetricCard`** is an elevated `KuiCard` (`shapes.large`);
  `KuiMetricValue` pairs an `overline` label with `KuiDataNumber`.
- **`KuiBadge`** is a 999dp pill with 8dp horizontal / 3dp vertical padding,
  `labelSmall` text, and tones mapped to `KuiColors` status containers; never
  pass ad-hoc colors.
- Market movement must use `KuiColors.up/down/flat` and their soft variants,
  plus a sign or arrow, so meaning survives without color.

YAML entries such as `button-ghost`, `badge-success`, and `market-up-chip` are
design variants of the primitives above, not separate composables.

`*Sample` composables (`KuiFeedbackSample`, `KuiNavigationSample`, etc.) are
`internal` preview fixtures, not public API.

## Accessibility & Motion

Color reinforces state but never carries it alone: status, market movement,
validation, and destructive actions also need text, an icon, or structure.
Keep primary touch targets at 44dp or larger, give icon buttons a
`contentDescription`, and keep focus and disabled states visible in all three
themes.

Motion comes from `MaterialExpressiveTheme`'s default `MotionScheme`, exposed
as `KuiTheme.motionScheme`; use it instead of hand-tuned durations. Use
`AnimatedVisibility` and `Modifier.animateItem()` for dynamic lists and
content. Navigation transitions belong to `KeelimNavDisplay` in
`core:component`. Motion should describe state change, not decorate.

## Web Parity

Android and web share role names but not every value. Differences observed at
the time of writing (Android vs. web `all-web-ui`); this list is not exhaustive,
so compare both sources before claiming parity:

| Role | Android | Web |
| --- | --- | --- |
| Danger | `#DC2626` | `#b91c1c` |
| Success | `#15803D` | `#166534` |
| Warning | `#B45309` | `#92400e` |
| Info | `#0369A1` | `#1d4ed8` |
| Market up | `#DC2626` | `#b91c1c` |
| Market flat | `#64748B` | `#475569` |
| Admin background | `#0A0A0A` | `#050505` |
| Admin surface | `#141414` | `#121212` |
| Admin border | `#334155` | `#4a4a4a` |
| Admin foreground | `#F8FAFC` | `#f7f7f7` |
| Admin muted | `#94A3B8` | `#c0c0c0` |
| Fonts | system sans / monospace | Pretendard / JetBrains Mono |
| `label` weight | 500 | 600 |
| `overline` | 500, 0.12em | 700, 0em |
| `numeric` weight | 400 | 500 |
| Radius sm / md / lg / xl | 14 / 18 / 24 / 28dp | 0.85 / 1.15 / 1.5 / 1.5rem (xl aliases lg) |
| Spacing | no 20dp / 40dp step | has 20px / 40px |

The comments in `Color.kt` say the values "mirror" web CSS; the table above is
what actually ships. Aligning the platforms is a deliberate change to
`core:designsystem` (and its consumers), not a documentation edit. Record the
decision before changing either side.

## Do's and Don'ts

Do read tokens through `KuiTheme` and use `Kui*` primitives before writing
screen-local styles. Do add a missing token or primitive to `core:designsystem`
first, then consume it. Do update this file in the same change as any token,
theme, or primitive API change.

Do not hardcode hex colors, `dp` spacing, or `sp` sizes in feature or app
modules. Do not use `MaterialTheme` directly in screens when a `KuiTheme`
accessor exists. Do not create theme or token copies in `core:component` or
app modules. Do not change shared tokens in a way that alters product
calculations, persisted records, or another app's screens without that app's
owner agreeing.
