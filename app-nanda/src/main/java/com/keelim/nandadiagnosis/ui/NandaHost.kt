@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.nandadiagnosis.ui

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.NandaRoute
import com.keelim.core.resource.Res
import com.keelim.core.resource.nanda_feature_preparing
import com.keelim.core.resource.nanda_move_action
import com.keelim.core.resource.nanda_move_confirmation
import com.keelim.nandadiagnosis.ui.screen.category.CategoriesType
import com.keelim.nandadiagnosis.ui.screen.category.CategoryRoute
import com.keelim.nandadiagnosis.ui.screen.diagnosis.DiagnosisRoute
import com.keelim.nandadiagnosis.ui.screen.exercise.ExerciseRoute
import com.keelim.nandadiagnosis.ui.screen.food.edit.FoodEditRoute
import com.keelim.nandadiagnosis.ui.screen.food.overview.FoodRoute
import com.keelim.nandadiagnosis.ui.screen.length.LengthScreen
import com.keelim.nandadiagnosis.ui.screen.main.MainBottomSheet
import com.keelim.nandadiagnosis.ui.screen.medication.MedicationRoute
import com.keelim.nandadiagnosis.ui.screen.nutrient.NutrientRoute
import com.keelim.nandadiagnosis.ui.screen.nutrient.timer.NutrientTimerRoute
import com.keelim.nandadiagnosis.ui.screen.water.WaterIntakeRoute
import com.keelim.setting.screen.event.EventRoute
import com.keelim.setting.navigation.registerSettingsEntries
import com.keelim.web.navigateToWebModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun NandaHost(
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    backStack: SnapshotStateList<Any>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val featurePreparingMessage = stringResource(Res.string.nanda_feature_preparing)
    val moveAction = stringResource(Res.string.nanda_move_action)

    KeelimNavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) {
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
                                    onShowSnackbar(featurePreparingMessage, null)
                                }
                            }
                        }
                    },
                    onMedicationClick = {
                        backStack.add(NandaRoute.Medication)
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
                            val result = onShowSnackbar(
                                getString(Res.string.nanda_move_confirmation, title),
                                moveAction,
                            )
                            if (result) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri.toUri()))
                            }
                        }
                    },
                    onNutrientTimerClick = {
                        backStack.add(NandaRoute.NutrientTimer)
                    },
                )
            }
            entry<NandaRoute.Web> { route ->
                context.navigateToWebModule(route.uri.toUri())
            }
            entry<NandaRoute.Length> {
                LengthScreen()
            }
            entry<NandaRoute.WaterIntake> {
                WaterIntakeRoute()
            }
            entry<NandaRoute.Medication> {
                MedicationRoute()
            }
            registerSettingsEntries(
                backStack = backStack,
                context = context,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
    }
}
