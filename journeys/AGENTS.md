# :journeys

## Focus

`journeys/` contains XML-defined end-to-end (E2E) Journey test suites for all six Android applications in the monorepo (`app-nanda`, `app-cnubus`, `app-comssa`, `app-my-grade`, `app-mysenior`, `app-arducon`). These suites adhere to the Android CLI Journey specification (`references/journeys.md`) and define user-facing interaction and verification sequences evaluated sequentially against live-running apps on Android emulators or devices.

## Suite Inventory

- `journeys/app-nanda-journey.xml`: Buddy Daily Wellness Journey (title "Buddy Daily", bottom tabs Today/Plan/Insights/Tools, navigate Plan, verify "This week’s plan", navigate Tools, verify "Privacy", back button returns to Today).
- `journeys/app-cnubus-journey.xml`: CNUBUS Route & Settings Journey ("A 노선", "B 노선", tap "설정", verify "앱 설정"/"홈페이지"/"맵 바로가기", tap "앱 설정", verify "Settings", back button returns to 설정 tab).
- `journeys/app-comssa-journey.xml`: Comssa Economic Calendar & Financial Calculators Journey ("캘린더", "환율", "금융", tap "금융", verify "금융 계산기", verify "복리 계산기", "대출 상환 계산기", tap "대출 상환 계산기", back button returns to 캘린더).
- `journeys/app-my-grade-journey.xml`: MyGrade Calculator & Pager Journey (title "MyGrade", verify "과목명", "원점수", "과목 평균", "표준편차", "학생 수", "Clear", "Submit", swipe left to Study Timer, back button returns to grade calculator).
- `journeys/app-mysenior-journey.xml`: MySenior Settings Navigation Journey ("Settings", "Theme Change", "알림내역", "실험실", "Device Info", tap "Device Info", verify device info details screen, back button returns to Settings).
- `journeys/app-arducon-journey.xml`: Arducon Dev Tools & JSON Formatter Journey (title "Arducon", subtitle "Deeplink Tester", tool categories "링크 테스트", "데이터 도구", "앱/환경 점검", tap "JSON Formatter", verify JSON Formatter screen, back, tap "Base64", verify Base64 screen, back to Arducon).

## Boundaries & Operational Rules

- Keep Journey XML definitions well-formed and valid XML (`xmllint --noout journeys/*.xml`).
- Actions must specify actual user-observable UI labels and text elements verified in the owning app's source code and `AGENTS.md`.
- External Journey evaluation runs through Android CLI (`android run`, `android layout`, `android screen`, `adb shell input`) and preserves the `:app-nanda` hard gate (no Compose test rules in `:app-nanda`).
- Never create a `bin/` directory anywhere in the monorepo (`test ! -d bin`).
