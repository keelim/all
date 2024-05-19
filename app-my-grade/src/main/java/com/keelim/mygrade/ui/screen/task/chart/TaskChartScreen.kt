package com.keelim.mygrade.ui.screen.task.chart

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.canvas.chart.PieChartEntry
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.util.randomColor

@Composable
fun TaskChartRoute(
    viewModel: TaskChartViewModel = hiltViewModel(),
) = trace("TaskChartRoute") {
    val state by viewModel.state.collectAsStateWithLifecycle(
  lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
)
    TaskChartScreen(
        state = state,
    )
}

@Composable
fun TaskChartScreen(
    state: SealedUiState<List<PieChartEntry>>,
) = trace("TaskChartScreen") {
    AnimatedContent(
        targetState = state,
        label = "",
    ) { targetState ->
        when (targetState) {
            SealedUiState.Loading -> Loading()
            is SealedUiState.Error -> EmptyView()
            is SealedUiState.Success -> TaskChartSuccessSection(targetState.value)
        }
    }
}

@Composable
fun TaskChartDetailEntry(
    title: String,
    color: Color,
) = trace("TaskChartDetailEntry") {
    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp),
                    ).size(40.dp),
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskChartScreen() {
    TaskChartSuccessSection(
        listOf(
            PieChartEntry("hello1", randomColor(), 10f),
            PieChartEntry("hello2", randomColor(), 10f),
            PieChartEntry("hello3", randomColor(), 10f),
            PieChartEntry("hello4", randomColor(), 10f),
            PieChartEntry("hello5", randomColor(), 10f),
        ),
    )
}
