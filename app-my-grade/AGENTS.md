# app-my-grade

## OVERVIEW
Comprehensive student utility app featuring grade calculation based on normal distribution, study timers with history, task management with analytics, and vocabulary learning tools.

## STRUCTURE
- `ui/screen/main/`: App dashboard and primary entry points.
- `ui/screen/grade/`: Core grade calculation, editing, and note-taking.
- `ui/screen/timer/`: Focus timer with persistent session history.
- `ui/screen/task/`: Task management including data visualization (charts).
- `ui/screen/analytics/`: Study pattern analysis and statistics.
- `ui/screen/word/`: Vocabulary flashcard and writing features.
- `ui/screen/water/`: Daily water intake tracking.

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Grade Calculation Logic | `ui/screen/main/MainRoute.kt` | Uses `normalProbability.grade()` |
| Navigation Setup | `ui/MyGradeHost.kt` | Uses `androidx.navigation3` |
| Grade History | `ui/screen/history/` | Persisted via `core:data` |
| Math Utilities | `libs.apache.math` | Used for probability/statistics |
| Ad Integration | `MainActivity.kt` | Play Services Ads |

## FEATURES
- **Grade Calculator**: Predicts grades using normal distribution (Apache Math) based on mean and standard deviation.
- **Study Timer**: Track study sessions with history export/viewing capabilities.
- **Analytics**: Visualizes study habits and task completion rates over time.
- **Vocabulary**: Simple flashcard system (`WordShow`) and entry system (`WordWrite`).
- **Water Tracker**: Basic daily hydration tracking.

## CONVENTIONS
- **Navigation**: Uses the experimental `androidx.navigation3` with a manual `backStack` (SnapshotStateList) in `MyGradeHost`.
- **Stateless Routes**: Every screen follows the `*Route` (stateless wrapper) -> `*Screen` (UI) pattern.
- **State Management**: ViewModels expose `StateFlow` or `uiState` objects.

## ANTI-PATTERNS
- **Direct DB Access**: Do not use Room DAOs directly; always go through `core:data` repositories.
- **Complex Logic in Routes**: Keep `*Route.kt` as simple entry points; move logic to `ViewModel`.

## NOTES
- Uses **Apache Commons Math** for core statistical calculations.
- Integrated with `feature:ui-setting` for global theme and notification management.
- DeepLink support via `deeplinkdispatch`.
