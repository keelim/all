package com.keelim.composeutil.component.custom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun formatTime(isLeadingZeroNeeded: Boolean = false, value: Int): String {
    return if (isLeadingZeroNeeded) {
        String.format("%02d", value)
    } else {
        String.format("%2d", value)
    }
}

@Composable
fun NumberPickerList(
    numbers: List<Int>,
    selectedItem: (Int) -> Unit,
) {
    val listState = rememberLazyListState(0, 0)

    val isScrollInProgress = remember {
        derivedStateOf { listState.isScrollInProgress }
    }
    val firstVisibleItemIndex = remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }
    val firstVisibleItemScrollOffset = remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset }
    }

    val textSizeState = remember { mutableIntStateOf(22) }

    Box(
        modifier = Modifier
            .width(40.dp)
            .height(96.dp),
    ) {
        LazyColumn(
            state = listState,
        ) {
            item {
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                )
            }
            itemsIndexed(items = numbers) { index, item ->
                if (firstVisibleItemIndex.value != item) {
                    textSizeState.intValue = 18
                } else {
                    textSizeState.intValue = 22
                    selectedItem(item)
                }
                Text(
                    text = formatTime(value = item),
                    fontSize = textSizeState.intValue.sp,
                    modifier = Modifier.height(32.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                )
            }
        }
        LaunchedEffect(isScrollInProgress.value) {
            if (!isScrollInProgress.value.not()) return@LaunchedEffect

            if (firstVisibleItemScrollOffset.value < 85 / 2) {
                if (listState.layoutInfo.totalItemsCount == 0) {
                    return@LaunchedEffect
                }
                listState.animateScrollToItem(index = firstVisibleItemIndex.value)
            } else {
                listState.animateScrollToItem(index = firstVisibleItemIndex.value + 1)
            }
        }
    }
}
