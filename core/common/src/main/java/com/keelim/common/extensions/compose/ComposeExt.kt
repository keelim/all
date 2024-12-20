package com.keelim.common.extensions.compose

import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView

private fun addContentToView(
    viewGroup: ViewGroup,
    content: @Composable (() -> Unit) -> Unit,
) {
    viewGroup.addView(
        ComposeView(viewGroup.context).apply {
            setContent {
                BottomSheetWrapper(viewGroup, this, content)
            }
        },
    )
}

@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    content: @Composable (() -> Unit) -> Unit,
) {
    val tag = parent::class.java.simpleName
    val coroutineScope = rememberCoroutineScope()
//    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var isSheetOpened by remember { mutableStateOf(false) }

//    ModalBottomSheetLayout(
//        sheetBackgroundColor = Color.Transparent,
//        sheetState = modalBottomSheetState,
//        sheetContent = {
//            content {
//                // Action passed for clicking close button in the content
//                coroutineScope.launch {
//                    modalBottomSheetState.hide() // will trigger the LaunchedEffect
//                }
//            }
//        }
//    ) {}

//    LaunchedEffectBackHandler {
//        coroutineScope.launch {
//            modalBottomSheetState.hide() // will trigger the LaunchedEffect
//        }
//    }

    // Take action based on hidden state
//    LaunchedEffect(modalBottomSheetState.currentValue) {
//        when (modalBottomSheetState.currentValue) {
//            ModalBottomSheetValue.Hidden -> {
//                when {
//                    isSheetOpened -> parent.removeView(composeView)
//                    else -> {
//                        isSheetOpened = true
//                        modalBottomSheetState.show()
//                    }
//                }
//            }
//            else -> {
//                Log.i(TAG, "Bottom sheet ${modalBottomSheetState.currentValue} state")
//            }
//        }
//    }
}

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun SetUp(noinline content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ComponentActivity.activityComposeView(
    noinline content: @Composable () -> Unit,
) {
    setContent {
        SetUp {
            content()
        }
    }
}
