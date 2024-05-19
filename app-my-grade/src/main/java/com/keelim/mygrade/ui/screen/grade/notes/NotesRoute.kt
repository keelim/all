@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.mygrade.ui.screen.grade.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space8
import com.keelim.core.database.model.Notices

@Composable
fun NotesRoute(
    viewModel: NotesViewModel = hiltViewModel(),
) = trace("NotesRoute") {
    val uiState by viewModel.notesUiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )
    NotesScreen(
        uiState = uiState,
        onDeleteClick = viewModel::deleteNote,
    )
}

@Composable
fun NotesScreen(
    uiState: SealedUiState<List<Notices>>,
    onDeleteClick: (Notices) -> Unit,
) = trace("NotesScreen") {
    AnimatedContent(
        targetState = uiState,
        label = "",
    ) { targetState ->
        when (targetState) {
            is SealedUiState.Error -> EmptyView()
            SealedUiState.Loading -> Loading()
            is SealedUiState.Success -> if (targetState.value.isEmpty()) {
                EmptyView()
            } else {
                NoteSuccessSection(
                    uiState = targetState,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }
}

@Composable
fun NoteSuccessSection(
    uiState: SealedUiState.Success<List<Notices>>,
    onDeleteClick: (Notices) -> Unit,
) = trace("NoteSuccessSection") {
    var isMarkedRequireDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (isMarkedRequireDialog) {
        BasicAlertDialog(
            onDismissRequest = { isMarkedRequireDialog = false },
        ) {
            Column {
                Text(
                    text = "노트를 활성화 해야 삭제할 수 있습니다. ",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.clickable {
                        isMarkedRequireDialog = false
                    },
                ) {
                    Text(
                        text = "확인",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
    LazyColumn {
        items(
            items = uiState.value,
            key = { it.uid },
        ) { item ->
            var isMarked by rememberSaveable(key = item.uid.toString()) {
                mutableStateOf(false)
            }
            NotesItem(
                isMarked = isMarked,
                data = item,
                onClick = {
                    isMarked = !isMarked
                },
                onLongClick = {
                    if (isMarked) {
                        onDeleteClick(item)
                    } else {
                        isMarkedRequireDialog = true
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun PreviewNotesScreen() {
    NotesScreen(
        uiState = SealedUiState.Success(
            value = listOf(
                Notices(
                    title = "hi",
                    note = "note",
                ),
                Notices(
                    title = "hi",
                    note = "notes",
                ),
            ),
        ),
        onDeleteClick = {},
    )
}

@Composable
fun NotesItem(
    isMarked: Boolean,
    data: Notices,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("NotesItem") {
    Box(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = onLongClick,
            )
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(space12)),
    ) {
        val boxStartPadding by animateDpAsState(
            targetValue = if (isMarked) space8 else 0.dp,
            label = "",
        )
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(start = boxStartPadding)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = data.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = data.note,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 1.dp),
                )
                Text(
                    text = data.createdAt.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNotesItem() {
    Column {
        NotesItem(
            isMarked = false,
            data = Notices(
                title = "hi",
                note = "note",
            ),
            onClick = {},
            onLongClick = {},
        )
        NotesItem(
            isMarked = true,
            data = Notices(
                title = "hi",
                note = "note",
            ),
            onClick = {},
            onLongClick = {},
        )
        NotesItem(
            isMarked = false,
            data = Notices(
                title = "hi",
                note = "note",
            ),
            onClick = {},
            onLongClick = {},
        )
        NotesItem(
            isMarked = true,
            data = Notices(
                title = "hi",
                note = "note",
            ),
            onClick = {},
            onLongClick = {},
        )
    }
}
