package com.keelim.mygrade.ui.screen.grade

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiSnackbar
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8

@Composable
fun GradeRoute(
    onNavigateNotes: () -> Unit,
    onEditClick: (String) -> Unit,
    onShareClick: () -> Unit,
    viewModel: GradeViewModel = hiltViewModel(),
) = trace("GradeRoute") {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data by viewModel.data.collectAsStateWithLifecycle()
    if (uiState.isMessageShow) {
        KuiSnackbar(dismissAction = { viewModel.dismissMessage() }) { KuiText(text = uiState.message) }
    }
    GradeScreen(
        subject = data.subject,
        grade = data.grade,
        rank = data.point,
        onNavigateNotes = onNavigateNotes,
        onEditClick = onEditClick,
        onShareClick = onShareClick,
    )
}

@Composable
private fun GradeScreen(
    subject: String,
    grade: String,
    rank: String,
    onNavigateNotes: () -> Unit,
    onEditClick: (String) -> Unit,
    onShareClick: () -> Unit,
) = trace("GradeScreen") {
    Column {
        NavigationBackArrowBar(title = "결과 확인")
        GradeContent(
            subject = subject,
            grade = grade,
            rank = rank,
            onNavigateNotes = onNavigateNotes,
            onEditClick = onEditClick,
            onShareClick = onShareClick,
        )
    }
}

@Composable
fun GradeContent(
    subject: String,
    grade: String,
    rank: String,
    onNavigateNotes: () -> Unit,
    onEditClick: (String) -> Unit,
    onShareClick: () -> Unit,
) = trace("GradeContent") {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            KuiText(text = "과목명: $subject ", style = KuiTheme.typography.headlineMedium)
            KuiText(text = "예상학점: $grade ", style = KuiTheme.typography.headlineMedium)
            KuiText(text = "예상등수: $rank", style = KuiTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(space12))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            KuiText(
                text = "+ 은 교수님 재량입니다.",
            )
        }
        Spacer(modifier = Modifier.height(space24))
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(
                    space = space24,
                    alignment = Alignment.CenterHorizontally,
                ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            KuiIcon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onNavigateNotes() },
            )

            KuiIcon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onEditClick(subject) },
            )

            val value by rememberInfiniteTransition(label = "")
                .animateFloat(
                    initialValue = 25f,
                    targetValue = -25f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 600,
                            easing = LinearEasing,
                        ),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "",
                )

            KuiIcon(
                imageVector = Icons.Filled.Share,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onShareClick() }
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(
                            pivotFractionX = 0.5f,
                            pivotFractionY = 0.5f,
                        )
                        rotationZ = value
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGradeScreen() {
    GradeScreen(
        subject = "Computer Science",
        grade = "12",
        rank = "23",
        onNavigateNotes = {},
        onEditClick = {},
        onShareClick = {},
    )
}
