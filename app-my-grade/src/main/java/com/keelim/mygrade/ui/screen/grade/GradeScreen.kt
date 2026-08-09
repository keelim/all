@file:OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mygrade.ui.screen.grade

import androidx.compose.animation.core.Animatable
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiFilledIconButton
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiSnackbar
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8
import com.keelim.mygrade.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
        NavigationBackArrowBar(title = stringResource(R.string.grade_result_title))
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
    val motionScheme = KuiTheme.motionScheme
    val shareScale = remember { Animatable(1f) }
    val shareAlpha = remember { Animatable(1f) }
    var shareFeedbackTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(shareFeedbackTrigger) {
        if (shareFeedbackTrigger == 0) return@LaunchedEffect

        shareScale.snapTo(1f)
        shareAlpha.snapTo(1f)
        coroutineScope {
            launch {
                shareScale.animateTo(
                    targetValue = 0.96f,
                    animationSpec = motionScheme.fastSpatialSpec(),
                )
            }
            launch {
                shareAlpha.animateTo(
                    targetValue = 0.78f,
                    animationSpec = motionScheme.fastEffectsSpec(),
                )
            }
        }
        coroutineScope {
            launch {
                shareScale.animateTo(
                    targetValue = 1f,
                    animationSpec = motionScheme.fastSpatialSpec(),
                )
            }
            launch {
                shareAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = motionScheme.fastEffectsSpec(),
                )
            }
        }
    }

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
            KuiText(
                text = stringResource(R.string.grade_subject_format, subject),
                style = KuiTheme.typography.titleMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            KuiText(
                text = stringResource(R.string.grade_expected_grade),
                style = KuiTheme.typography.labelLarge,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            KuiText(
                text = grade,
                style = KuiTheme.typography.displayLarge,
                color = KuiTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            KuiText(
                text = stringResource(R.string.grade_expected_rank_format, rank),
                style = KuiTheme.typography.titleLarge,
                color = KuiTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.height(space12))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            KuiText(
                text = stringResource(R.string.grade_disclaimer),
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant,
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
            KuiIconButton(onClick = onNavigateNotes) {
                KuiIcon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.grade_notes_action),
                    modifier = Modifier.size(24.dp),
                )
            }

            KuiIconButton(onClick = { onEditClick(subject) }) {
                KuiIcon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.grade_edit_action),
                    modifier = Modifier.size(24.dp),
                )
            }

            KuiFilledIconButton(
                onClick = {
                    shareFeedbackTrigger += 1
                    onShareClick()
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = shareScale.value
                    scaleY = shareScale.value
                    alpha = shareAlpha.value
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = KuiTheme.colorScheme.primaryContainer,
                    contentColor = KuiTheme.colorScheme.onPrimaryContainer,
                ),
            ) {
                KuiIcon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.grade_share_action),
                    modifier = Modifier.size(24.dp),
                )
            }
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
