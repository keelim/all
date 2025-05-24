package com.keelim.nandadiagnosis.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace

@Composable
fun MainBottomSheet(
    onBlogClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDismiss: () -> Unit,
    modalBottomSheetState: SheetState = rememberModalBottomSheetState(),
) = trace("MainBottomSheet") {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        MainBottomSheetScreen(
            onBlogClick = onBlogClick,
            onAboutClick = onAboutClick,
        )
    }
}

@Composable
private fun MainBottomSheetScreen(
    onBlogClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) = trace("MainBottomSheetScreen") {
    Column {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 36.dp, horizontal = 20.dp)
                .height(40.dp)
                .clickable { onBlogClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "관련 정보", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
            )
        }
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onAboutClick()
                },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainBottomSheetScreen() {
    MainBottomSheetScreen()
}
