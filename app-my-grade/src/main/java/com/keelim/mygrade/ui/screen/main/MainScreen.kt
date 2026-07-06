@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.mygrade.ui.screen.main

import android.Manifest
import android.os.Build
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.rounded.Create
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.component.fab.FabButtonMain
import com.keelim.composeutil.component.fab.FabButtonState
import com.keelim.composeutil.component.fab.FabButtonSub
import com.keelim.composeutil.component.fab.MultiMainFab
import com.keelim.composeutil.component.pager.HorizontalPagerIndicator
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.mygrade.ui.screen.timer.TimerRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

private const val pageCount = 2

@Composable
fun MainRoute(
    onSubmitClick: (String, NormalProbability, Int) -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
    onLabClick: () -> Unit,
    onNavigateTimerHistory: () -> Unit,
    onNavigateTask: () -> Unit,
    onNavigateAnalytics: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    timerPresetHours: Int? = null,
    timerPresetMinutes: Int? = null,
    timerPresetSeconds: Int? = null,
) = trace("MainRoute") {
    val mainState by viewModel.mainScreenState.collectAsStateWithLifecycle()
    val subject by viewModel.subject.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val origin by viewModel.origin.collectAsStateWithLifecycle()
    val average by viewModel.average.collectAsStateWithLifecycle()
    val number by viewModel.number.collectAsStateWithLifecycle()
    val student by viewModel.student.collectAsStateWithLifecycle()
    MainScreen(
        timerPresetHours = timerPresetHours,
        timerPresetMinutes = timerPresetMinutes,
        timerPresetSeconds = timerPresetSeconds,
        clear = viewModel::clear,
        submit = viewModel::submit,
        moveState = viewModel::moveState,
        updateEditType = viewModel::updateEditType,
        onSubmitClick = onSubmitClick,
        onFloatingButtonClick1 = onFloatingButtonClick1,
        onFloatingButtonClick2 = onFloatingButtonClick2,
        onLabClick = onLabClick,
        onNavigateTimerHistory = onNavigateTimerHistory,
        onNavigateTask = onNavigateTask,
        onNavigateAnalytics = onNavigateAnalytics,
        mainState = mainState,
        subject = subject,
        state = state,
        origin = origin,
        average = average,
        number = number,
        student = student,
    )
}

private val appPermissions: List<String> = buildList {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun MainScreen(
    timerPresetHours: Int? = null,
    timerPresetMinutes: Int? = null,
    timerPresetSeconds: Int? = null,
    mainState: MainScreenState,
    subject: String,
    state: MainState,
    origin: String,
    average: String,
    number: String,
    student: String,
    clear: () -> Unit,
    submit: () -> Unit,
    moveState: (MainState) -> Unit,
    updateEditType: (EditType) -> Unit,
    onSubmitClick: (String, NormalProbability, Int) -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
    onLabClick: () -> Unit,
    onNavigateTimerHistory: () -> Unit,
    onNavigateTask: () -> Unit,
    onNavigateAnalytics: () -> Unit,
) = trace("MainScreen") {
    val pagerState = rememberPagerState(pageCount = { pageCount })
    var backPressedState by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    SimpleAcquirePermissions(
        permissions = appPermissions,
    ) {
    }
    PredictiveBackHandler(
        enabled = backPressedState,
    ) { progress ->
        try {
            progress.collect()
            if (pagerState.currentPage == 0) {
                backPressedState = false
            } else {
                scope.launch {
                    pagerState.animateScrollToPage(page = 0)
                }
            }
        } catch (e: CancellationException) {
            // no-op
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space12, vertical = space12),
    ) {
        MainTopSection(
            pagerState = pagerState,
            onLabClick = onLabClick,
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            if (page == 1) {
                LaunchedEffect(page) {
                    backPressedState = true
                }
                TimerRoute(
                    presetHours = timerPresetHours,
                    presetMinutes = timerPresetMinutes,
                    presetSeconds = timerPresetSeconds,
                    onNavigateTimerHistory = onNavigateTimerHistory,
                )
            } else {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (state is MainState.Success) {
                        SideEffect {
                            onSubmitClick(
                                state.subject,
                                state.value,
                                state.student,
                            )
                            moveState(MainState.UnInitialized)
                        }
                    }
                    ScoreTextRow(
                        text = "과목명",
                        value = subject,
                        onValueChange = { updateEditType(EditType.Subject(it)) },
                        isError = mainState.subjectError,
                    )
                    ScoreTextRow(
                        text = "원점수",
                        value = origin,
                        onValueChange = { updateEditType(EditType.Origin(it)) },
                        isError = mainState.originError,
                    )
                    ScoreTextRow(
                        text = "과목 평균",
                        value = average,
                        onValueChange = { updateEditType(EditType.Average(it)) },
                        isError = mainState.averageError,
                    )
                    ScoreTextRow(
                        text = "표준편차",
                        value = number,
                        onValueChange = { updateEditType(EditType.Number(it)) },
                        isError = mainState.numberError,
                    )
                    ScoreTextRow(
                        text = "학생 수",
                        value = student,
                        onValueChange = { updateEditType(EditType.Student(it)) },
                        isError = mainState.studentError,
                    )
                    MainBottomSection(
                        onClearClick = clear,
                        onSubmitClick = submit,
                        onFloatingButtonClick1 = onFloatingButtonClick1,
                        onFloatingButtonClick2 = onFloatingButtonClick2,
                        onNavigateWord = onNavigateTask,
                        onNavigateAnalytics = onNavigateAnalytics,
                    )
                }
            }
        }
    }
}

@Composable
private fun MainTopSection(
    pagerState: PagerState,
    onLabClick: () -> Unit,
) = trace("MainTopSection") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuiText(text = "MyGrade", style = KuiTheme.typography.headlineLarge)
        Spacer(
            modifier = Modifier.width(space8),
        )
        KuiIcon(
            Icons.Filled.Build,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .clickable { onLabClick() },
        )
        Spacer(
            modifier = Modifier.weight(1f),
        )
        HorizontalPagerIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            targetPage = pagerState.targetPage,
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
        )
    }
}

@Composable
private fun ColumnScope.MainBottomSection(
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
    onNavigateWord: () -> Unit,
    onNavigateAnalytics: () -> Unit,
) = trace("MainBottomSection") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        KuiButton(onClick = onClearClick) {
            KuiText(text = "Clear", style = KuiTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.width(space4))
        KuiButton(onClick = onSubmitClick) {
            KuiText(text = "Submit", style = KuiTheme.typography.labelLarge)
        }
    }
    Spacer(modifier = Modifier.weight(1f))
    val items by remember {
        mutableStateOf(
            listOf(
                History(),
                Analytics(),
                Other(),
                Setting(),
            ),
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        var fabState by remember { mutableStateOf<FabButtonState>(FabButtonState.Collapsed) }
        MultiMainFab(
            fabState = fabState,
            items = items,
            fabIcon = FabButtonMain(),
            fabOption = FabButtonSub(
                backgroundTint = KuiTheme.colorScheme.primary,
                iconTint = KuiTheme.colorScheme.onPrimary,
            ),
            onFabItemClicked = { item ->
                when (item) {
                    is History -> onFloatingButtonClick1()
                    is Setting -> onFloatingButtonClick2()
                    is Other -> onNavigateWord()
                    is Analytics -> onNavigateAnalytics()
                }
            },
            stateChanged = {
                fabState = it
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onSubmitClick = { _, _, _ -> },
        onFloatingButtonClick1 = {},
        onFloatingButtonClick2 = {},
        onLabClick = {},
        onNavigateTimerHistory = {},
        onNavigateTask = {},
        updateEditType = {},
        moveState = {},
        clear = {},
        submit = {},
        mainState = MainScreenState(),
        subject = "Computer Science",
        state = MainState.Success(
            flag = true,
            subject = "Computer Science",
            value = NormalProbability(1),
        ),
        origin = "23",
        average = "23",
        number = "23",
        student = "23",
        onNavigateAnalytics = {},
    )
}

@Composable
internal fun ScoreTextRow(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) = trace("ScoreTextRow") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        KuiText(text = text, style = KuiTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.width(20.dp))
        KuiFilledTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = if (isError) {
                {
                    KuiText(
                        text = "형식을 다시 써주세요",
                        style = KuiTheme.typography.labelSmall,
                    )
                }
            } else {
                null
            },
            placeholder = {
                KuiText(
                    text = "$text 입력해주세요.",
                    style = KuiTheme.typography.labelLarge,
                )
            },
            leadingIcon = { KuiIcon(imageVector = Icons.Rounded.Create, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    Spacer(modifier = Modifier.height(36.dp))
}

@Preview(showBackground = true)
@Composable
private fun PreviewScoreTextRow() {
    ScoreTextRow(text = "원점수", value = "", onValueChange = {}, isError = false)
}

data class History(
    override val imageVector: ImageVector = Icons.AutoMirrored.Filled.List,
    override val label: String = "히스토리 확인",
) : FabButtonItem

data class Setting(
    override val imageVector: ImageVector = Icons.Filled.Settings,
    override val label: String = "설정",
) : FabButtonItem

data class Other(
    override val imageVector: ImageVector = Icons.Filled.ThumbUp,
    override val label: String = "Task",
) : FabButtonItem

data class Analytics(
    override val imageVector: ImageVector = Icons.Filled.Build,
    override val label: String = "Analytics",
) : FabButtonItem
