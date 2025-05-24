@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.nandadiagnosis.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.NandaRoute
import com.keelim.nandadiagnosis.ui.screen.category.CategoriesType
import com.keelim.nandadiagnosis.ui.screen.category.CategoryRoute
import com.keelim.nandadiagnosis.ui.screen.diagnosis.DiagnosisRoute
import com.keelim.nandadiagnosis.ui.screen.exercise.ExerciseRoute
import com.keelim.nandadiagnosis.ui.screen.food.edit.FoodEditRoute
import com.keelim.nandadiagnosis.ui.screen.food.overview.FoodRoute
import com.keelim.nandadiagnosis.ui.screen.main.MainBottomSheet
import com.keelim.nandadiagnosis.ui.screen.nutrient.NutrientRoute
import com.keelim.nandadiagnosis.ui.screen.nutrient.timer.NutrientTimerRoute
import com.keelim.setting.screen.event.EventRoute
import com.keelim.setting.screen.settings.settingsEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NandaHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val backStack = rememberMutableStateListOf<AppRoute>(NandaRoute.Category)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(
                    targetScale = 0.7f,
                ),
            )
        },
        entryProvider = entryProvider {
            entry<NandaRoute.Category> {
                CategoryRoute(
                    onCategoryClick = { index, category ->
                        backStack.add(NandaRoute.Diagnosis(index.toString(), category))
                    },
                    onEditTypeClick = { type ->
                        when (type) {
                            CategoriesType.EXERCISE -> {
                                backStack.add(NandaRoute.Exercise)
                            }

                            CategoriesType.FOOD -> {
                                backStack.add(NandaRoute.Food)
                            }

                            else -> {
                                coroutineScope.launch {
                                    onShowSnackbar("현재 업데이트 준비중입니다. ", null)
                                }
                            }
                        }
                    },
                )
                if (bottomSheetState.isVisible) {
                    MainBottomSheet(
                        onBlogClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                        },
                        onAboutClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                            backStack.add(FeatureRoute.Settings)
                        },
                        onDismiss = { coroutineScope.launch { bottomSheetState.hide() } },
                        modalBottomSheetState = bottomSheetState,
                    )
                }
            }
            entry<NandaRoute.Diagnosis> {
                DiagnosisRoute(
                    onDiagnosisClick = {},
                )
            }
            entry<FeatureRoute.Event> {
                EventRoute()
            }
            entry<NandaRoute.Exercise> {
                ExerciseRoute()
            }
            entry<NandaRoute.Food> {
                FoodRoute(
                    onEditClick = {
                        backStack.add(NandaRoute.FoodEdit(title = it))
                    },
                )
            }
            entry<NandaRoute.FoodEdit> {
                FoodEditRoute()
            }
            entry<NandaRoute.NutrientTimer> {
                NutrientTimerRoute()
            }
            entry<NandaRoute.Nutrient> {
                NutrientRoute(
                    onNutrientClick = { title, uri ->
                        coroutineScope.launch {
                            val result = onShowSnackbar("$title 로 이동하시겠습니까?", "move")
                            if (result) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                            }
                        }
                    },
                    onNutrientTimerClick = {
                        backStack.add(NandaRoute.NutrientTimer)
                    },
                )
            }
            settingsEntry(
                backStack = backStack,
                context = context,
            )
        },
    )
}
