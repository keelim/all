package com.keelim.mygrade.ui.screen.grade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar

@Composable
fun GradeRoute(
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    viewModel: GradeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data by viewModel.data.collectAsStateWithLifecycle()
    if (uiState.isMessageShow) {
        Snackbar(dismissAction = { viewModel.dismissMessage() }) { Text(text = uiState.message) }
    }
    GradeScreen(
        grade = data.grade,
        rank = data.point,
        onCopyClick = onCopyClick,
        onShareClick = onShareClick
    )
}

@Composable
private fun GradeScreen(
    grade: String,
    rank: String,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Column {
        NavigationBackArrowBar(title = "결과 확인")
        GradeContent(
            grade = grade,
            rank = rank,
            onCopyClick = onCopyClick,
            onShareClick = onShareClick
        )
    }
}

@Composable
fun GradeContent(
    grade: String,
    rank: String,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "예상학점: $grade ", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "예상등수: $rank", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "+ 은 교수님 재량입니다.",
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onCopyClick() }
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onShareClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGradeScreen() {
    GradeScreen(grade = "12", rank = "23", onCopyClick = {}, onShareClick = {})
}
