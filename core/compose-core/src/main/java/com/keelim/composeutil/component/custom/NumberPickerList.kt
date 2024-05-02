package com.keelim.composeutil.component.custom

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.keelim.composeutil.resource.space32
import kotlinx.coroutines.flow.drop
import java.util.Locale

private fun formatTime(isLeadingZeroNeeded: Boolean = false, value: Int): String =
    String.format(locale = Locale.getDefault(), if (isLeadingZeroNeeded) "%02d" else "%2d", value)

@Composable
fun NumberPickerList(
    numbers: List<Int>,
    selectedItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
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

    Box(
        modifier = modifier
            .width(40.dp)
            .height(96.dp),
    ) {
        LazyColumn(
            state = listState,
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(space32),
                )
            }
            itemsIndexed(items = numbers) { index, item ->
                val animateState =
                    animateFloatAsState(
                        targetValue = if (firstVisibleItemIndex.value != item) 0f else 1f,
                        label = ""
                    )

                val textStyle = lerp(
                    MaterialTheme.typography.titleLarge,
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    animateState.value,
                )
                LaunchedEffect(firstVisibleItemIndex.value, item) {
                    if (firstVisibleItemIndex.value == item) {
                        selectedItem(item)
                    }
                }

                Text(
                    text = formatTime(value = item),
                    style = textStyle,
                    modifier = Modifier
                        .height(space32)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(space32),
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

        val view = LocalView.current
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .drop(1)
                .collect { index ->
                    when (index) {
                        0, listState.layoutInfo.totalItemsCount - 1 -> {
                            view.performHapticFeedback(
                                HapticFeedbackConstantsCompat.LONG_PRESS
                            )
                        }

                        else -> {
                            view.performHapticFeedback(
                                HapticFeedbackConstantsCompat.CLOCK_TICK
                            )
                        }
                    }
                }
        }
    }
}
