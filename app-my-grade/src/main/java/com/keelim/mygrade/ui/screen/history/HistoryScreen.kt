@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.mygrade.ui.screen.history

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.mygrade.ui.screen.main.NormalProbability
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HistoryRoute(
    onHistoryClick: (NormalProbability, Int) -> Unit,
) {
    HistoryScreen(
        onHistoryClick = onHistoryClick,
    )
}

@Composable
internal fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onHistoryClick: (NormalProbability, Int) -> Unit = { _, _ -> },
) {
    val histories by viewModel.histories.collectAsStateWithLifecycle(persistentListOf())
    if (histories.isEmpty()) {
        EmptyView()
    } else {
        HistoryList(histories = histories, onHistoryClick = onHistoryClick)
    }
}

@Composable
fun HistoryList(
    histories: PersistentList<GradeHistory>,
    listState: LazyListState = rememberLazyListState(),
    onHistoryClick: (NormalProbability, Int) -> Unit = { _, _ -> },
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = histories,
            key = { history: GradeHistory -> history },
        ) { history ->
            GradeHistoryItem(
                history = history,
                onHistoryClick = onHistoryClick,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHistoryList() {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GradeHistoryItem(
            GradeHistory(
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        GradeHistoryItem(
            GradeHistory(
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        GradeHistoryItem(
            GradeHistory(
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        GradeHistoryItem(
            GradeHistory(
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
        GradeHistoryItem(
            GradeHistory(
                date = "sociosqu",
                grade = "placerat",
                myGrade = 3495,
                totalStudent = 9776,
            ),
        )
    }
}

@Composable
fun GradeHistoryItem(
    history: GradeHistory,
    onHistoryClick: (NormalProbability, Int) -> Unit = { _, _ -> },
) {
    Card(
        onClick = { onHistoryClick(NormalProbability(1), 1) },
        shape = ShapeDefaults.Large,
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 20.dp),
        ) {
            Text(
                text = "예상 학점: ${history.grade}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "예상 등수: ${history.myGrade} / ${history.totalStudent} 등",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "입력 날짜: ${history.date}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGradeHistoryItem() {
    GradeHistoryItem(
        history = GradeHistory(
            date = "utamur",
            grade = "suscipiantur",
            myGrade = 5812,
            totalStudent = 7712,
        ),
    )
}
