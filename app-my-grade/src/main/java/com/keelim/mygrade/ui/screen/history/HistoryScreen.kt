package com.keelim.mygrade.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HistoryRoute(
    onHistoryClick: (String, String, String) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) = trace("HistoryRoute") {
    val histories by viewModel.histories.collectAsStateWithLifecycle(persistentListOf())
    HistoryScreen(
        histories = histories,
        onHistoryClick = onHistoryClick,
    )
}

@Composable
internal fun HistoryScreen(
    histories: List<GradeHistory>,
    onHistoryClick: (String, String, String) -> Unit = { _, _, _ -> },
) = trace("HistoryScreen") {
    if (histories.isEmpty()) {
        EmptyView()
    } else {
        Column {
            NavigationBackArrowBar(title = "히스토리 확인")
            Spacer(modifier = Modifier.height(10.dp))
            HistoryList(histories = histories, onHistoryClick = onHistoryClick)
        }
    }
}

@Composable
fun HistoryList(
    histories: List<GradeHistory>,
    listState: LazyListState = rememberLazyListState(),
    onHistoryClick: (String, String, String) -> Unit = { _, _, _ -> },
) = trace("HistoryList") {
    LazyColumn(
        state = listState,
        modifier = Modifier.padding(horizontal = space12, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        items(items = histories) { history ->
            HistoryCard(
                history = history,
                onHistoryClick = onHistoryClick,
            )
        }
    }
}

@Preview
@Composable
fun PreviewHistoryList() {
    Column(
        modifier = Modifier.padding(horizontal = space12),
        verticalArrangement = Arrangement.spacedBy(space12),
    ) {
        HistoryCard(
            GradeHistory(
                subject = "Computer Science 1",
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        HistoryCard(
            GradeHistory(
                subject = "Computer Science 2",
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        HistoryCard(
            GradeHistory(
                subject = "Computer Science 3",
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        HistoryCard(
            GradeHistory(
                subject = "Computer Science 4",
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        HistoryCard(
            GradeHistory(
                subject = "Computer Science 5",
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
    }
}

@Composable
fun HistoryCard(
    history: GradeHistory,
    onHistoryClick: (String, String, String) -> Unit = { _, _, _ -> },
) = trace("HistoryCard") {
    // val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onHistoryClick(history.subject, history.grade, "${history.myGrade} / ${history.totalStudent}") },
    ) {
        Column {
            Row(
                modifier = Modifier.padding(space16),
                horizontalArrangement = Arrangement.spacedBy(space16),
            ) {
                Column {
                    Text(
                        text = "과목명: ${history.subject}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(space4))
                    Text(
                        text = "예상 학점: ${history.grade}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(space4))
                    Text(
                        text = "예상 등수: ${history.myGrade} / ${history.totalStudent} 등",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Text(
                text = "입력 날짜: ${history.date}",
                maxLines = 4,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = space16),
            )
            Spacer(Modifier.height(space16))
            // AsyncImage(
            //     model = ImageRequest.Builder(context)
            //         .data("https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80")
            //         .crossfade(true)
            //         .build(),
            //     modifier = Modifier
            //         .aspectRatio(16 / 9f),
            //     contentScale = ContentScale.Crop,
            //     contentDescription = null
            // )
        }
    }
}
