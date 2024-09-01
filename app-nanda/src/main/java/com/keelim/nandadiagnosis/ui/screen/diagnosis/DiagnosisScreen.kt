package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.nandadiagnosis.R

@Composable
fun DiagnosisRoute(
    onDiagnosisClick: () -> Unit,
    viewModel: DiagnosisViewModel = hiltViewModel(),
) = trace("DiagnosisRoute") {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    DiagnosisScreen(
        screenState = screenState,
        onDiagnosisClick = onDiagnosisClick,
    )

    val context = LocalContext.current
    LaunchedEffect(context) {
        viewModel.initArray(context.resources.getStringArray(R.array.diagnosis1))
    }
}

@Composable
fun DiagnosisScreen(
    screenState: DiagnosisScreenState,
    onDiagnosisClick: () -> Unit,
) = trace("DiagnosisScreen") {
    DiagnosisStateView(state = screenState, onDiagnosisClick = onDiagnosisClick)
}

@Composable
private fun DiagnosisStateView(
    state: DiagnosisScreenState,
    onDiagnosisClick: () -> Unit,
) = trace("DiagnosisStateView") {
    when (state) {
        DiagnosisScreenState.Error,
        DiagnosisScreenState.Empty,
        -> EmptyView()

        DiagnosisScreenState.Loading -> Loading()
        is DiagnosisScreenState.Success -> {
            LazyColumn {
                items(state.items) {
                    DiagnosisItem(
                        title = it.diagnosis,
                        content = "",
                        label = "",
                        onDiagnosisClick = onDiagnosisClick,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDiagnosisScreen() {
    DiagnosisScreen(
        screenState = DiagnosisScreenState.Empty,
        onDiagnosisClick = {},
    )
}

@Composable
fun DiagnosisItem(
    title: String,
    content: String,
    label: String,
    onDiagnosisClick: () -> Unit,
) = trace("DiagnosisItem") {
    Surface(onClick = { onDiagnosisClick() }, shape = MaterialTheme.shapes.large) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space16),
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
        ) {
            Column(Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(text = label, style = MaterialTheme.typography.labelLarge)
                }
                Spacer(Modifier.height(space4))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDiagnosisItem() {
    DiagnosisItem(
        title = "Alice Bennett",
        content =
        "I miss you! It's been too long since we last caught up. Let's plan a coffee date soon!",
        label = "4d ago",
        onDiagnosisClick = {},
    )
}
