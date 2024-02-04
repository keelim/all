package com.keelim.composeutil.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
data class ShareItem(
    val imageVector: ImageVector,
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun ShareSheet(
    shareItems: List<ShareItem>,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
) {
    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.expand()
    }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 24.dp),
            ) {
                Text(
                    text = "Share!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    items(shareItems) { item ->
                        LabeledIconButton(
                            imageVector = item.imageVector,
                            label = item.label,
                            onClick = item.onClick,
                        )
                    }
                }
            }
        },
    ) { }
}

@Preview
@Composable
fun PreviewShareSheet() {
    ShareSheet(
        shareItems = listOf(
            ShareItem(imageVector = Icons.Rounded.AccountBox, label = "explicari", onClick = {}),
            ShareItem(imageVector = Icons.Rounded.AccountBox, label = "explicari", onClick = {}),
            ShareItem(imageVector = Icons.Rounded.AccountBox, label = "explicari", onClick = {}),
            ShareItem(imageVector = Icons.Rounded.AccountBox, label = "explicari", onClick = {}),
            ShareItem(imageVector = Icons.Rounded.AccountBox, label = "explicari", onClick = {}),
        ),
    )
}

@Composable
private fun LabeledIconButton(
    imageVector: ImageVector,
    label: String,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = contentColorFor(containerColor),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}
