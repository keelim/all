# Copilot Instructions for Android Project

## 1. Languages & Frameworks
- Use Kotlin as the primary language; Java is allowed if necessary.
- Actively use Android Jetpack (Compose, ViewModel, LiveData, etc.).
- Use the Gradle build system.

## 2. Code Style
- Follow the Google Android Kotlin Style Guide.
- Use camelCase for functions, variables, and class names.
- File names should use UpperCamelCase.
- Indentation: 4 spaces.

## 3. Naming Conventions
- ViewModel: `*ViewModel`
- Repository: `*Repository`
- Data classes: `*Model`, `*Entity`
- Composable functions: UpperCamelCase

## 4. Architecture
- Use the MVVM pattern.
- Separate the data layer with the Repository pattern.
- UI should be based on Compose.
- Use Hilt for dependency injection.

## 5. Restrictions & Cautions
- Do not hardcode strings; use string resources.
- Do not use deprecated APIs.
- Use context only within the required scope.
- Do not perform network/DB operations on the UI thread.
- Avoid unnecessary global variables.

## 6. Testing
- Use JUnit and MockK for unit tests.
- Use `androidx.ui.test` for Composable tests.

## 7. Others
- When adding external libraries, specify them in Gradle dependencies.
