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
    - Currency Converter
    - Tax Calculator
    - Save and Share Calculation Results
- **Tech Stack:** Jetpack Compose for UI, DataStore for history persistence.
