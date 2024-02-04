@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.mygrade.ui.screen.main

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.component.fab.FabButtonMain
import com.keelim.composeutil.component.fab.FabButtonState
import com.keelim.composeutil.component.fab.FabButtonSub
import com.keelim.composeutil.component.fab.MultiMainFab
import com.keelim.composeutil.component.pager.HorizontalPagerIndicator
import com.keelim.mygrade.ui.screen.timer.TimerScreen
import kotlinx.coroutines.launch

private const val pageCount = 2

@Composable
fun MainRoute(
    onSubmitClick: (String, NormalProbability, Int) -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
    onLabClick: () -> Unit,
    onNavigateTimerHistory: () -> Unit,
    onNavigateTask: () -> Unit,
) = trace("MainRoute") {
    MainScreen(
        onSubmitClick = onSubmitClick,
        onFloatingButtonClick1 = onFloatingButtonClick1,
        onFloatingButtonClick2 = onFloatingButtonClick2,
        onLabClick = onLabClick,
        onNavigateTimerHistory = onNavigateTimerHistory,
        onNavigateTask = onNavigateTask,
    )
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onSubmitClick: (String, NormalProbability, Int) -> Unit = { _, _, _ -> },
    onFloatingButtonClick1: () -> Unit = {},
    onFloatingButtonClick2: () -> Unit = {},
    onLabClick: () -> Unit = {},
    onNavigateTimerHistory: () -> Unit = {},
    onNavigateTask: () -> Unit = {},
) = trace("MainScreen") {
    val mainState by viewModel.mainScreenState.collectAsStateWithLifecycle()
    val subject by viewModel.subject.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val origin by viewModel.origin.collectAsStateWithLifecycle()
    val average by viewModel.average.collectAsStateWithLifecycle()
    val number by viewModel.number.collectAsStateWithLifecycle()
    val student by viewModel.student.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { pageCount })
    var backPressedState by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    BackHandler(
        enabled = backPressedState,
        onBack = {
            if (pagerState.currentPage == 0) {
                backPressedState = false
            } else {
                scope.launch {
                    pagerState.animateScrollToPage(page = 0)
                }
            }
        },
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 12.dp),
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
                TimerScreen(
                    onNavigateTimerHistory = onNavigateTimerHistory,
                )
            } else {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (state is MainState.Success) {
                        SideEffect {
                            onSubmitClick(
                                (state as MainState.Success).subject,
                                (state as MainState.Success).value,
                                (state as MainState.Success).student,
                            )
                            viewModel.moveState(MainState.UnInitialized)
                        }
                    }
                    ScoreTextRow(
                        text = "과목명",
                        value = subject,
                        onValueChange = { viewModel.updateEditType(EditType.Subject(it)) },
                        isError = mainState.subjectError,
                    )
                    ScoreTextRow(
                        text = "원점수",
                        value = origin,
                        onValueChange = { viewModel.updateEditType(EditType.Origin(it)) },
                        isError = mainState.originError,
                    )
                    ScoreTextRow(
                        text = "과목 평균",
                        value = average,
                        onValueChange = { viewModel.updateEditType(EditType.Average(it)) },
                        isError = mainState.averageError,
                    )
                    ScoreTextRow(
                        text = "표준편차",
                        value = number,
                        onValueChange = { viewModel.updateEditType(EditType.Number(it)) },
                        isError = mainState.numberError,
                    )
                    ScoreTextRow(
                        text = "학생 수",
                        value = student,
                        onValueChange = { viewModel.updateEditType(EditType.Student(it)) },
                        isError = mainState.studentError,
                    )
                    MainBottomSection(
                        onClearClick = viewModel::clear,
                        onSubmitClick = viewModel::submit,
                        onFloatingButtonClick1 = onFloatingButtonClick1,
                        onFloatingButtonClick2 = onFloatingButtonClick2,
                        onNavigateWord = onNavigateTask,
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
        Text(text = "MyGrade", style = MaterialTheme.typography.headlineLarge)
        Spacer(
            modifier = Modifier.width(8.dp),
        )
        Icon(
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
) = trace("MainBottomSection") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Button(onClick = onClearClick) {
            Text(text = "Clear", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(onClick = onSubmitClick) {
            Text(text = "Submit", style = MaterialTheme.typography.labelLarge)
        }
    }
    Spacer(modifier = Modifier.weight(1f))
    val items by remember {
        mutableStateOf(
            listOf(
                History(),
                Setting(),
                Other(),
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
                backgroundTint = MaterialTheme.colorScheme.primary,
                iconTint = MaterialTheme.colorScheme.onPrimary,
            ),
            onFabItemClicked = { item ->
                when (item) {
                    is History -> onFloatingButtonClick1()
                    is Setting -> onFloatingButtonClick2()
                    is Other -> onNavigateWord()
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
fun PreviewMainScreen() {
    MainScreen()
}

@Composable
internal fun ScoreTextRow(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) = trace("ScoreTextRow") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.width(20.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = if (isError) {
                {
                    Text(
                        text = "형식을 다시 써주세요",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            } else {
                null
            },
            placeholder = {
                Text(
                    text = "$text 입력해주세요.",
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Create, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    Spacer(modifier = Modifier.height(36.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewScoreTextRow() {
    ScoreTextRow(text = "원점수", value = "", onValueChange = {}, isError = false)
}

data class History(
    override val imageVector: ImageVector = Icons.Filled.List,
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
