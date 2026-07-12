package com.keelim.nandadiagnosis.ui.screen.nutrient.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import com.keelim.core.designsystem.component.KuiBasicAlertDialog
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCircularProgressIndicator
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keelim.common.extensions.toUiTwoDigits
import com.keelim.composeutil.component.custom.NumberPickerList
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.Res
import com.keelim.core.resource.nanda_timer_completed
import com.keelim.core.resource.nanda_timer_ends_at
import com.keelim.core.resource.nanda_timer_hour_unit
import com.keelim.core.resource.nanda_timer_minute_unit
import com.keelim.core.resource.nanda_timer_progress_percent
import com.keelim.core.resource.nanda_timer_running
import com.keelim.core.resource.nanda_timer_second_unit
import com.keelim.core.resource.nanda_timer_start
import com.keelim.core.resource.nanda_timer_stop
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun NutrientTimerRoute() = trace("NutrientTimerRoute") {
    NutrientTimerScreen()
}

@Composable
private fun NutrientTimerScreen(
    viewModel: NutrientTimerViewModel = hiltViewModel(),
) = trace("NutrientTimerScreen") {
    val isCountDownTimerVisible = viewModel.isRunning
    val addedTime = remember(isCountDownTimerVisible) {
        viewModel.addTime(System.currentTimeMillis())
    }
    val dialogState = remember { mutableStateOf(false) }

    if (dialogState.value) {
        ShowDialog { dialogState.value = false }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(space24),
    ) {
        Spacer(modifier = Modifier.height(space16))
        SelectTime(runningState = isCountDownTimerVisible, viewModel = viewModel)
        CircularCountDownTimer(
            runningState = isCountDownTimerVisible,
            viewModel = viewModel,
            addedTime = addedTime,
            dialogState = dialogState,
        )

        Spacer(modifier = Modifier.height(space24))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            KuiButton(
                onClick = {
                    when (viewModel.isRunning) {
                        RunningState.STOPPED -> viewModel.start()
                        RunningState.STARTED -> viewModel.stop()
                    }
                },
            ) {
                KuiText(
                    text = stringResource(
                        if (viewModel.isRunning == RunningState.STOPPED) {
                            Res.string.nanda_timer_start
                        } else {
                            Res.string.nanda_timer_stop
                        },
                    ),
                    style = KuiTheme.typography.labelLarge,
                    color = KuiTheme.colorScheme.onPrimary,
                )
            }
        }
        Spacer(
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun SelectTime(
    runningState: RunningState,
    viewModel: NutrientTimerViewModel,
    modifier: Modifier = Modifier,
) = trace("SelectTime") {
    if (runningState == RunningState.STOPPED) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(350.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row {
                    NumberPickerList(numbers = HOUR_LIST, selectedItem = { viewModel.hour = it })

                    KuiText(
                        text = stringResource(Res.string.nanda_timer_hour_unit),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                }

                Row {
                    NumberPickerList(numbers = MINUTE_LIST, { viewModel.minute = it })

                    KuiText(
                        text = stringResource(Res.string.nanda_timer_minute_unit),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                }
                Row {
                    NumberPickerList(numbers = SECOND_LIST, { viewModel.second = it })

                    KuiText(
                        text = stringResource(Res.string.nanda_timer_second_unit),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
fun CircularCountDownTimer(
    runningState: RunningState,
    viewModel: NutrientTimerViewModel,
    dialogState: MutableState<Boolean>,
    addedTime: String,
    modifier: Modifier = Modifier,
) = trace("CircularCountDownTimer") {
    val leftTime = viewModel.leftTime.intValue
    val totalTime = viewModel.getTotalTimeInSeconds().coerceAtLeast(1)
    val targetProgress = if (runningState == RunningState.STARTED) {
        (leftTime.toFloat() / totalTime).coerceIn(0f, 1f)
    } else {
        0f
    }
    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = if (runningState == RunningState.STARTED && leftTime < totalTime) {
            tween(durationMillis = 1_000, easing = LinearEasing)
        } else {
            snap()
        },
        label = "nutrientTimerProgress",
    )
    val runningLabel = stringResource(Res.string.nanda_timer_running)
    val progressDescription = stringResource(
        Res.string.nanda_timer_progress_percent,
        (targetProgress * 100).roundToInt(),
    )

    LaunchedEffect(runningState, leftTime) {
        if (runningState == RunningState.STARTED && leftTime == 0) {
            dialogState.value = true
            viewModel.stop()
        }
    }

    if (runningState != RunningState.STOPPED) {
        Box(
            modifier = modifier.size(350.dp),
            contentAlignment = Alignment.Center,
        ) {
            KuiCircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = KuiTheme.colors.surfaceStrong,
                strokeWidth = 10.dp,
            )

            KuiCircularProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxSize()
                    .semantics {
                        contentDescription = progressDescription
                        stateDescription = runningLabel
                        progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
                    },
                color = KuiTheme.colors.info,
                strokeWidth = 10.dp,
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                KuiText(
                    text = "${(leftTime / 3600).toUiTwoDigits()}:" +
                        "${
                            ((leftTime / 60) % 60).toUiTwoDigits()
                        }:" +
                        (leftTime % 60).toUiTwoDigits(),
                    style = KuiTheme.typography.displaySmall,
                    color = KuiTheme.colorScheme.onSurface,
                )
                KuiText(
                    text = runningLabel,
                    style = KuiTheme.typography.labelLarge,
                    color = KuiTheme.colors.info,
                )

                Spacer(modifier = Modifier.height(space24))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    KuiIcon(
                        imageVector = Icons.Default.AccountBox,
                        modifier = Modifier.padding(space4),
                        contentDescription = null,
                    )
                    KuiText(
                        text = stringResource(Res.string.nanda_timer_ends_at, addedTime),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimerScreen() {
    NutrientTimerScreen()
}

@Composable
private fun ShowDialog(
    onDismiss: () -> Unit,
) = trace("ShowDialog") {
    KuiBasicAlertDialog(onDismissRequest = onDismiss) {
        KuiText(
            text = stringResource(Res.string.nanda_timer_completed),
            modifier = Modifier.padding(space8),
            style = KuiTheme.typography.titleLarge,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}
