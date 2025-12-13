# all Project
all is a native Android mobile application written in Kotlin. It is a multi-module project that contains several apps.
## Architecture
This project is a modern Android application that follows the official architecture guidance from Google. It is a reactive, single-activity app that uses the following:
- **UI:** Built entirely with Jetpack Compose, including Material 3 components and adaptive layouts for different screen sizes.
- **State Management:** Unidirectional Data Flow (UDF) is implemented using Kotlin Coroutines and `Flow`s. `ViewModel`s act as state holders, exposing UI state as streams of data.
- **Dependency Injection:** Hilt is used for dependency injection throughout the app, simplifying the management of dependencies and improving testability.
- **Navigation:** Navigation is handled by Jetpack Navigation for Compose, allowing for a declarative and type-safe way to navigate between screens.
- **Data:** The data layer is implemented using the repository pattern.
- **Local Data:** Room and DataStore are used for local data persistence.
- **Remote Data:** Retrofit and OkHttp are used for fetching data from the network.
- **Background Processing:** WorkManager is used for deferrable background tasks.
## Modules
The main Android apps live in the `app-*/` folders. Feature modules live in `feature/` and core and shared modules in `core/`.
## Continuous integration
- The workflows are defined in `.github/workflows/*.yml` and they contain various checks.
## Version control and code location
- The project uses git and is hosted on [GitHub](https://github.com/keelim/all).

## Apps

### app-comssa
#### Financial Calculators
- **Description:** Provides various financial calculation tools.
- **Key Features:**
    - Compound Interest Calculator
    - Loan Repayment Calculator
    - Investment Return Calculator
    - Retirement Calculator
    - Currency Converter
    - Tax Calculator
    - Save and Share Calculation Results
- **Tech Stack:** Jetpack Compose for UI, DataStore for history persistence.

#### Ecocal (Economic Calendar)
- **Description:** Provides important economic events and calendar.
- **Key Features:**
    - Economic Events Calendar
    - Country-specific Filter
    - Importance Level Filter

#### FlashCard
- **Description:** A simple flashcard study tool.
- **Key Features:**
    - Create and Study Flashcards
    - Randomized Reviews
    - Progress Tracking

#### Calendar
- **Description:** General purpose calendar view.
- **Key Features:**
    - Month View
    - Event Marking

### app-nanda
#### Nanda Diagnosis & Health Tools
- **Description:** Provides NANDA nursing diagnosis information along with various health tracking and utility tools.
- **Key Features:**
    - **Diagnosis**: Search and browse NANDA nursing diagnoses with detailed information.
    - **Food Manager**: Track daily food intake and manage nutritional data.
    - **Exercise Tracker**: Log and monitor exercise routines.
    - **Nutrient Info**: Database of nutrient information.
    - **Unit Converter**: Utility tools for length and other conversions.
    - **Water Intake Tracker**: Daily hydration tracking with animated progress ring, quick add buttons (100ml, 200ml, 250ml, 500ml), weekly history chart, and intake records list.
- **Tech Stack:** Jetpack Compose for UI, Room (Shared Module) for data persistence, Hilt for DI.

### app-cnubus
#### CNU Bus
- **Description:** Provides real-time bus information for Chungnam National University.
- **Key Features:**
    - Real-time Bus Locations
    - Route Maps (A, B, C, Night Routes)
    - **Favorites**: Save frequently used bus stops for quick access.
    - **Search**: Quickly find bus stops by name.
    - **Modern UI**: Clean, card-based interface with floating search bar and intuitive settings.
    - Settings and Shortcuts
- **Tech Stack:** Jetpack Compose for UI (Material 3), Hilt for DI, Google Maps, DataStore (for Favorites).

### app-arducon
#### DeepLink Tester & Utility App
- **Description:** A utility application for testing deep links and managing development tools.
- **Key Features:**
    - **DeepLink Management**: Create, read, update, and delete deep links. Support for categories and search.
    - **QR Code Scanner & Generator**: Scan QR codes to open links and generate QR codes from deep links.
    - **OG Tag Preview**: Preview Open Graph tags for URLs.
    - **SaaS Status**: Monitor service status.
    - **Statistics**: View usage statistics with charts.
    - **JSON Formatter**: Format and validate JSON strings.
    - **Base64 Tool**: Encode/Decode Base64 strings with history persistence.
    - **Device Info**: View detailed device information (Model, SDK, Screen, etc.).
    - **URL Shortener**: Shorten URLs locally, track click analytics, and set expiration dates.
- **Tech Stack:** Jetpack Compose for UI (Material 3), Hilt for DI, Room/DataStore for persistence, Coil for image loading, Jsoup for OG tag parsing.

### app-my-grade
#### Grade Calculator & Manager
- **Description:** A comprehensive tool for calculating school grades, managing academic history, and tracking study tasks.
- **Key Features:**
    - **Grade Calculation**: Calculate grades based on raw scores and student distribution using standard deviation principles.
    - **History**: Save and review past grade calculations.
    - **Timer & Task**: Integrated focus timer and task management with visual charts.
    - **Timer History**: View, edit, and manage past timer sessions. Supports swipe-to-delete, description editing, and tap-to-restore timer settings.
    - **Word**: Simple vocabulary management system.
    - **Study Analytics**: Dashboard with activity heatmap, weekly study charts, subject distribution, streak tracking, and gamification elements.
- **Tech Stack:** Jetpack Compose, Hilt, Room, Apache Commons Math (for statistics).

## Conventions

### UI Guidelines

#### Text Composable
- **Rule**: All `Text` Composables MUST explicitly specify `style` (typography) and `color`.
- **Reason**: To ensure consistent typography and color usage across the application and prevent accidental fallback to default styles that might not match the design system.
- **Example**:
  ```kotlin
  Text(
      text = "Example",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
  )
  ```

#### Animation
- **Rule**: All user actions (e.g., list item deletion, visibility changes) MUST be animation-friendly.
- **Reason**: To provide a smooth and engaging user experience.
- **Example**: Use `LazyColumn` with `animateItem` for lists, or `AnimatedVisibility` for visibility changes.

#### Build Verification
- **Rule**: After making code changes to any module, always run `./gradlew :<module>:assembleDebug` to verify the build.
- **Reason**: To catch compile errors early and ensure the code integrates correctly.
- **Example**:
  ```bash
  ./gradlew :app-my-grade:assembleDebug
  ./gradlew :core:data:assembleDebug
  ./gradlew :feature:ui-setting:assembleDebug
  ```

## Feature Modules

### feature/ui-setting
#### ThemeScreen
- **Description**: Modern theme selection screen with animated UI.
- **Key Features**:
    - **Header Section**: Gradient icon with palette and clear title/subtitle.
    - **Theme Selection Cards**: Inline card-based theme selection (Light/Dark) with scale animation and border animation on selection.
    - **Preview Panel**: Live preview of selected theme with animated color transitions.
    - **Smooth Animations**: Uses `animateFloatAsState` for scale, `animateColorAsState` for colors with `tween` duration.
- **Tech Stack**: Jetpack Compose (Material 3), Material Icons Extended, Animation APIs.

