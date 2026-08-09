@file:OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mygrade.ui.screen.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DateRange
import com.keelim.core.designsystem.component.KuiBottomSheetScaffold
import com.keelim.core.designsystem.component.KuiButton
import androidx.compose.material3.ButtonDefaults
import com.keelim.core.designsystem.component.KuiDropdownMenu
import com.keelim.core.designsystem.component.KuiDropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiFilledIconButton
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiIcon
import androidx.compose.material3.IconButtonDefaults
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.SheetValue
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.common.extensions.formatUiTime
import com.keelim.common.extensions.toUiTwoDigits
import com.keelim.composeutil.component.custom.NumberPickerList
import com.keelim.mygrade.R
import kotlinx.coroutines.launch

// region Stateful Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerRoute(
    presetHours: Int? = null,
    presetMinutes: Int? = null,
    presetSeconds: Int? = null,
    onNavigateTimerHistory: () -> Unit,
    onNavigateAnalytics: () -> Unit = {},
    viewModel: TimerViewModel = hiltViewModel(),
) = trace("TimerRoute") {
    val timerUiState by viewModel.timerUiState.collectAsStateWithLifecycle()

    // Apply preset values if provided
    LaunchedEffect(presetHours, presetMinutes, presetSeconds) {
        if (presetHours != null) viewModel.hour = presetHours
        if (presetMinutes != null) viewModel.minute = presetMinutes
        if (presetSeconds != null) viewModel.second = presetSeconds
    }

    TimerScreen(
        isRunning = timerUiState.runningState,
        timerUiState = timerUiState,
        leftTime = timerUiState.leftTime,
        totalTimeSeconds = viewModel.getTotalTimeInSeconds(),
        addedTime = viewModel.addTime(System.currentTimeMillis()),
        onNavigateTimerHistory = onNavigateTimerHistory,
        onNavigateAnalytics = onNavigateAnalytics,
        onResetTimer = viewModel::clear,
        onStart = viewModel::start,
        onStop = viewModel::stop,
        onTimerComplete = viewModel::onTimerComplete,
        onClearDialog = viewModel::clearDialog,
        onHourChange = { viewModel.hour = it },
        onMinuteChange = { viewModel.minute = it },
        onSecondChange = { viewModel.second = it },
    )
}

// endregion

// region Stateless Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    isRunning: RunningState,
    timerUiState: TimerUiState,
    leftTime: Int,
    totalTimeSeconds: Int,
    addedTime: String,
    onNavigateTimerHistory: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTimerComplete: () -> Unit,
    onClearDialog: () -> Unit,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit,
    onNavigateAnalytics: () -> Unit = {},
    onResetTimer: () -> Unit = {},
) = trace("TimerScreen") {
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState,
    )

    // Show bottom sheet for unset timer
    LaunchedEffect(timerUiState.isUnsetDialog) {
        if (timerUiState.isUnsetDialog) {
            bottomSheetState.expand()
        }
    }

    KuiBottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = KuiTheme.colorScheme.surfaceContainerHigh,
        sheetContent = {
            TimerBottomSheetContent(
                isTimerComplete = !timerUiState.isUnsetDialog,
                onDismiss = {
                    scope.launch {
                        bottomSheetState.hide()
                        onClearDialog()
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            KuiTheme.colorScheme.surface,
                            KuiTheme.colorScheme.surfaceContainerLowest,
                        ),
                    ),
                )
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Header
                TimerHeader(
                    onNavigateTimerHistory = onNavigateTimerHistory,
                    onNavigateAnalytics = onNavigateAnalytics,
                    onResetTimer = onResetTimer,
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Main Content
                AnimatedContent(
                    targetState = isRunning,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f))
                            .togetherWith(fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.9f))
                    },
                    label = "TimerContent",
                ) { runningState ->
                    when (runningState) {
                        RunningState.STOPPED -> {
                            TimePickerSection(
                                onHourChange = onHourChange,
                                onMinuteChange = onMinuteChange,
                                onSecondChange = onSecondChange,
                            )
                        }

                        RunningState.STARTED -> {
                            CountdownSection(
                                isRunning = isRunning,
                                leftTime = leftTime,
                                totalTimeSeconds = totalTimeSeconds,
                                addedTime = addedTime,
                                onComplete = {
                                    scope.launch {
                                        onTimerComplete()
                                        bottomSheetState.expand()
                                        onStop()
                                    }
                                },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Control Button
                TimerControlButton(
                    isRunning = isRunning,
                    onStart = onStart,
                    onStop = onStop,
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

// endregion

// region Private Components

@Composable
private fun TimerHeader(
    onNavigateTimerHistory: () -> Unit,
    onNavigateAnalytics: () -> Unit = {},
    onResetTimer: () -> Unit = {},
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            KuiText(
                text = "Focus Timer",
                style = KuiTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = KuiTheme.colorScheme.onSurface,
            )
            KuiText(
                text = "집중 시간을 측정하세요",
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
        }

        Box {
            KuiFilledIconButton(
                onClick = { menuExpanded = true },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = KuiTheme.colorScheme.secondaryContainer,
                ),
            ) {
                KuiIcon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(R.string.timer_menu_action),
                    tint = KuiTheme.colorScheme.onSecondaryContainer,
                )
            }

            KuiDropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                KuiDropdownMenuItem(
                    text = {
                        KuiText(
                            text = "📊 Study Analytics",
                            style = KuiTheme.typography.bodyMedium,
                            color = KuiTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onNavigateAnalytics()
                    },
                )
                KuiDropdownMenuItem(
                    text = {
                        KuiText(
                            text = "📜 Timer History",
                            style = KuiTheme.typography.bodyMedium,
                            color = KuiTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onNavigateTimerHistory()
                    },
                )
                KuiHorizontalDivider()
                KuiDropdownMenuItem(
                    text = {
                        KuiText(
                            text = "🔄 Reset Timer",
                            style = KuiTheme.typography.bodyMedium,
                            color = KuiTheme.colorScheme.error,
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onResetTimer()
                    },
                )
            }
        }
    }
}

@Composable
private fun TimePickerSection(
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit,
) {
    KuiSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = KuiTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KuiIcon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = KuiTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            KuiText(
                text = "시간 설정",
                style = KuiTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = KuiTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TimePickerColumn(
                    label = "시간",
                    numbers = HOUR_LIST,
                    onValueChange = onHourChange,
                )
                TimePickerDivider()
                TimePickerColumn(
                    label = "분",
                    numbers = MINUTE_LIST,
                    onValueChange = onMinuteChange,
                )
                TimePickerDivider()
                TimePickerColumn(
                    label = "초",
                    numbers = SECOND_LIST,
                    onValueChange = onSecondChange,
                )
            }
        }
    }
}

@Composable
private fun TimePickerColumn(
    label: String,
    numbers: List<Int>,
    onValueChange: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KuiText(
            text = label,
            style = KuiTheme.typography.labelMedium,
            color = KuiTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        KuiSurface(
            shape = RoundedCornerShape(16.dp),
            color = KuiTheme.colorScheme.surfaceContainerHighest,
        ) {
            NumberPickerList(
                numbers = numbers,
                selectedItem = onValueChange,
            )
        }
    }
}

@Composable
private fun TimePickerDivider() {
    KuiText(
        text = ":",
        style = KuiTheme.typography.headlineLarge,
        color = KuiTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Light,
    )
}

@Composable
private fun CountdownSection(
    isRunning: RunningState,
    leftTime: Int,
    totalTimeSeconds: Int,
    addedTime: String,
    onComplete: () -> Unit,
) {
    val totalTime = totalTimeSeconds.toFloat()
    val progressRatio = if (totalTime > 0) leftTime / totalTime else 1f

    // Smooth animated progress
    val animatedProgress = remember { Animatable(1f) }
    val glowAlpha = remember { Animatable(0.18f) }
    val motionScheme = KuiTheme.motionScheme

    LaunchedEffect(leftTime) {
        if (leftTime == 0) {
            onComplete()
        }
    }

    // Animate progress based on leftTime ratio
    LaunchedEffect(progressRatio) {
        animatedProgress.animateTo(
            targetValue = progressRatio,
            animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        )
    }

    LaunchedEffect(isRunning) {
        if (isRunning == RunningState.STARTED) {
            glowAlpha.snapTo(0.18f)
            glowAlpha.animateTo(
                targetValue = 0.44f,
                animationSpec = motionScheme.fastEffectsSpec(),
            )
            glowAlpha.animateTo(
                targetValue = 0.18f,
                animationSpec = motionScheme.fastEffectsSpec(),
            )
        } else {
            glowAlpha.snapTo(0f)
        }
    }

    val primaryColor = KuiTheme.colorScheme.primary
    val backgroundColor = KuiTheme.colorScheme.surfaceContainerHighest
    val glowColor = KuiTheme.colorScheme.primary.copy(alpha = glowAlpha.value)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center,
        ) {
            // Custom Canvas for smooth progress ring
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                val strokeWidth = 14.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val center = Offset(size.width / 2, size.height / 2)

                // Outer glow effect
                drawCircle(
                    color = glowColor,
                    radius = radius + strokeWidth / 2,
                    center = center,
                    style = Stroke(width = strokeWidth + 8.dp.toPx()),
                )

                // Background ring
                drawCircle(
                    color = backgroundColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                // Progress arc - starts from top (-90 degrees) and goes clockwise
                val sweepAngle = 360f * animatedProgress.value
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }

            // Center content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                KuiText(
                    text = formatTimeDisplay(leftTime),
                    style = KuiTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = KuiTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(16.dp))

                KuiSurface(
                    shape = RoundedCornerShape(20.dp),
                    color = KuiTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        KuiIcon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = KuiTheme.colorScheme.onSecondaryContainer,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        KuiText(
                            text = addedTime,
                            style = KuiTheme.typography.bodyMedium,
                            color = KuiTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }
    }
}

private fun formatTimeDisplay(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "${formatUiTime(hour = hours, minute = minutes)}:${secs.toUiTwoDigits()}"
}

@Composable
private fun TimerControlButton(
    isRunning: RunningState,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val isStarted = isRunning == RunningState.STARTED

    KuiButton(
        onClick = if (isStarted) onStop else onStart,
        modifier = Modifier
            .height(64.dp)
            .width(200.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isStarted) {
                KuiTheme.colorScheme.error
            } else {
                KuiTheme.colorScheme.primary
            },
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp,
        ),
    ) {
        KuiIcon(
            imageVector = if (isStarted) Icons.Rounded.Close else Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        KuiText(
            text = if (isStarted) "중지" else "시작",
            style = KuiTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun TimerBottomSheetContent(
    isTimerComplete: Boolean,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Icon
        KuiSurface(
            shape = CircleShape,
            color = if (isTimerComplete) {
                KuiTheme.colorScheme.primaryContainer
            } else {
                KuiTheme.colorScheme.errorContainer
            },
            modifier = Modifier.size(80.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                KuiIcon(
                    imageVector = if (isTimerComplete) {
                        Icons.Default.PlayArrow
                    } else {
                        Icons.Rounded.DateRange
                    },
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (isTimerComplete) {
                        KuiTheme.colorScheme.onPrimaryContainer
                    } else {
                        KuiTheme.colorScheme.onErrorContainer
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        KuiText(
            text = if (isTimerComplete) "타이머 완료! 🎉" else "시간을 설정해주세요",
            style = KuiTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = KuiTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        KuiText(
            text = if (isTimerComplete) {
                "집중 시간이 기록되었습니다"
            } else {
                "타이머를 시작하기 전에 시간을 설정해주세요"
            },
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        KuiButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            KuiText(
                text = "확인",
                style = KuiTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// endregion

// region Preview

@Preview(showBackground = true)
@Composable
private fun PreviewTimerScreen() {
    TimerScreen(
        isRunning = RunningState.STOPPED,
        timerUiState = TimerUiState(),
        leftTime = 0,
        totalTimeSeconds = 0,
        addedTime = "12:00:00 PM",
        onNavigateTimerHistory = {},
        onStart = {},
        onStop = {},
        onTimerComplete = {},
        onClearDialog = {},
        onHourChange = {},
        onMinuteChange = {},
        onSecondChange = {},
    )
}
