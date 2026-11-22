---
version: alpha
name: Buddy Daily
description: Dark Material 3 design for the Buddy Daily Android wellness app.
colors:
  primary: "#42C4DD"
  on-primary: "#001F27"
  primary-container: "#173D4A"
  on-primary-container: "#C5F3FC"
  secondary: "#FFB000"
  on-secondary: "#2B1700"
  background: "#071625"
  on-background: "#F4F7FA"
  surface: "#102132"
  on-surface: "#F4F7FA"
  surface-variant: "#172A3D"
  on-surface-variant: "#AEBAC6"
  outline: "#3A4E62"
  outline-variant: "#26394C"
  error: "#FF8A80"
  on-error: "#3B0805"
typography:
  headline-medium: { fontFamily: 'Roboto, sans-serif', fontSize: 28px, lineHeight: 36px, fontWeight: 400 }
  headline-small: { fontFamily: 'Roboto, sans-serif', fontSize: 24px, lineHeight: 32px, fontWeight: 400 }
  title-large: { fontFamily: 'Roboto, sans-serif', fontSize: 22px, lineHeight: 28px, fontWeight: 400 }
  title-medium: { fontFamily: 'Roboto, sans-serif', fontSize: 16px, lineHeight: 24px, fontWeight: 500 }
  body-large: { fontFamily: 'Roboto, sans-serif', fontSize: 16px, lineHeight: 24px, fontWeight: 400 }
  body-medium: { fontFamily: 'Roboto, sans-serif', fontSize: 14px, lineHeight: 20px, fontWeight: 400 }
  body-small: { fontFamily: 'Roboto, sans-serif', fontSize: 12px, lineHeight: 16px, fontWeight: 400 }
  label-large: { fontFamily: 'Roboto, sans-serif', fontSize: 14px, lineHeight: 20px, fontWeight: 500 }
  label-medium: { fontFamily: 'Roboto, sans-serif', fontSize: 12px, lineHeight: 16px, fontWeight: 500 }
rounded:
  card: 20px
  check-in-card: 22px
  care-choice: 44px
spacing:
  screen-inset: 20px
  today-section-gap: 22px
  care-section-gap: 14px
  check-in-content-gap: 12px
  choice-gap: 8px
components:
  app-bar:
    backgroundColor: "{colors.background}"
    textColor: "{colors.on-surface}"
    typography: "{typography.title-large}"
  today-heading:
    backgroundColor: "{colors.background}"
    textColor: "{colors.on-background}"
    typography: "{typography.headline-medium}"
  navigation-item:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface-variant}"
    typography: "{typography.label-medium}"
  navigation-item-selected:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    typography: "{typography.label-medium}"
  content-card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.card}"
  check-in-card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    typography: "{typography.title-large}"
    rounded: "{rounded.check-in-card}"
    padding: "{spacing.screen-inset}"
  check-in-button:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label-large}"
    height: 54px
  care-choice:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.care-choice}"
    size: 88px
  care-choice-selected:
    backgroundColor: "{colors.primary-container}"
    rounded: "{rounded.care-choice}"
    size: 88px
  progress-track:
    backgroundColor: "{colors.surface-variant}"
  validation-message:
    backgroundColor: "{colors.background}"
    textColor: "{colors.error}"
    typography: "{typography.body-medium}"
---

# Buddy Daily

## Overview

Buddy Daily is a private Android wellness journal for daily check-ins, weekly routines, trends, and personal tools. Use calm, direct guidance and a dark Material 3 canvas. The shipping Compose theme and screens are the source for this reference.

## Colors

Deep navy is the page background. Use the lighter navy surface for cards and the bottom navigation bar. Cyan marks the primary action, selection, and progress. Reserve amber for routine guidance, and use salmon for validation feedback. Pair filled colors with their Material `on-*` roles. Use muted blue-gray for supporting text and subdued outlines for idle controls.

## Typography

Use the existing Material 3 headline, title, body, and label roles. The type scale uses Roboto for mockups and a system sans-serif fallback for Korean. Pixel values describe the reference mockup at 1×. Compose uses `sp` for text and `dp` for geometry.

## Layout

Use a scrollable single-column mobile layout with a top app bar and four bottom tabs in this order. Today, Plan, Insights, Tools (`오늘`, `플랜`, `인사이트`, `더보기`).

On Today, place the daily check-in card first so the start or edit button is visible without scrolling on the reference mobile viewport. Below it, show the care-weather header and date, the centered 228 dp care-weather orb, the three condition choices and optional tags, then the routine actions. Keep 20 dp screen insets, 22 dp between Today sections, and 8 dp between the three equal-width condition choices.

## Shapes

Use 20 dp corners for content cards and 22 dp for the Today check-in card. Each care-condition icon sits in an 88 dp circular surface with a 1 dp idle border or a 2 dp selected border.

## Motion & Micro-interaction

Animate the condition selection's colors over 220 ms. Fade in updated check-in content over 220 ms while the previous content fades out over 120 ms. Preserve the thicker selected border and radio selection semantics.

## Voice & Content Strategy

Use short, supportive, localized language. The Korean care-weather choices are `편안해요`, `조금 신경 쓰여요`, and `확인이 필요해요`. Keep saved, empty, and hidden states visually distinct. Use `숨김` when a recorded check-in value is masked.

## Components

- **Top app bar** displays the app name and a labeled lock toggle that communicates the current privacy state.
- **Daily check-in card** shows a start button when empty and an edit button when saved. The saved card keeps its sleep, stress, and energy labels visible while privacy mode masks their values.
- **Care weather** pairs an illustration with three single-select conditions. Selecting a condition saves it. The optional sweat, friction, and dryness chips become available after a condition is selected.
- **Bottom navigation** uses an icon and a text label for each tab. The active label uses primary cyan.

## Do's and Don'ts

- Show privacy mode enabled in the baseline mockup. Use synthetic values for all screen references, including screenshots provided to Stitch.
- Preserve the existing hierarchy and localized labels when exploring visual changes. Recheck the fit with Korean labels and Android font scaling.
- Update this file alongside accepted changes to the Compose theme or component rules. Source references are [NandaTheme.kt](src/main/java/com/keelim/nandadiagnosis/ui/theme/NandaTheme.kt), [WellnessScreen.kt](src/main/java/com/keelim/nandadiagnosis/wellness/ui/WellnessScreen.kt), and [BuddyScreen.kt](src/main/java/com/keelim/nandadiagnosis/wellness/ui/BuddyScreen.kt).
