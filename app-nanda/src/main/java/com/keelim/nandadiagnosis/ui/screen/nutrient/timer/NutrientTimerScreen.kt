package com.keelim.nandadiagnosis.ui.screen.nutrient.timer

import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keelim.common.extensions.toUiTwoDigits
import com.keelim.composeutil.component.custom.NumberPickerList
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun NutrientTimerRoute() = trace("NutrientTimerRoute") {
    NutrientTimerScreen()
}

@Composable
private fun NutrientTimerScreen(
    viewModel: NutrientTimerViewModel = hiltViewModel(),
) = trace("NutrientTimerScreen") {
    val isCountDownTimerVisible = viewModel.isRunning
    val addedTime = viewModel.addTime(System.currentTimeMillis())
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
                    text =
                    if (viewModel.isRunning == RunningState.STOPPED) {
                        "Start"
                    } else {
                        "Stop"
                    },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .alpha(0.4f)
                    .align(Alignment.Center),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row {
                    NumberPickerList(numbers = HOUR_LIST, selectedItem = { viewModel.hour = it })

                    KuiText(text = "h", modifier = Modifier.align(Alignment.CenterVertically))
                }

                Row {
                    NumberPickerList(numbers = MINUTE_LIST, { viewModel.minute = it })

                    KuiText(text = "m", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row {
                    NumberPickerList(numbers = SECOND_LIST, { viewModel.second = it })

                    KuiText(text = "s", modifier = Modifier.align(Alignment.CenterVertically))
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
    if (runningState != RunningState.STOPPED) {
        val leftTime = viewModel.leftTime.intValue

        val progress = remember(leftTime, viewModel) {
            Animatable(leftTime / viewModel.getTotalTimeInSeconds().toFloat())
        }

        Box(
            modifier = modifier.size(350.dp),
            contentAlignment = Alignment.Center,
        ) {
            KuiCircularProgressIndicator(
                progress = { 100f },
                modifier = Modifier.fillMaxSize(),
                color = Color.LightGray,
                strokeWidth = 10.dp,
            )

            KuiCircularProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val anim = remember { Animatable(0f) }

                LaunchedEffect(leftTime) {
                    if (leftTime == 0) {
                        dialogState.value = true
                        viewModel.stop()
                    } else {
                        anim.animateTo(1f)
                    }
                }

                KuiText(
                    modifier = Modifier
                        .alpha(anim.value)
                        .scale(anim.value / 2f + .5f),
                    text = "${(leftTime / 3600).toUiTwoDigits()}:" +
                        "${
                            ((leftTime / 60) % 60).toUiTwoDigits()
                        }:" +
                        (leftTime % 60).toUiTwoDigits(),
                    fontSize = 48.sp,
                )

                Spacer(modifier = Modifier.height(space24))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    KuiIcon(
                        imageVector = Icons.Default.AccountBox,
                        modifier = Modifier.padding(space4),
                        contentDescription = null,
                    )
                    KuiText(
                        text = addedTime,
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
            text = "확인해 주세요",
            modifier = Modifier.padding(space8),
            style = KuiTheme.typography.titleLarge,
        )
    }
}
