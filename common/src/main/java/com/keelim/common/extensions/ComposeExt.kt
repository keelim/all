package com.keelim.common.extensions

import android.app.Activity
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme

fun Activity.showAsBottomSheet(@IdRes id: Int, content: @Composable (() -> Unit) -> Unit) {
    val viewGroup = this.findViewById(id) as ViewGroup
    addContentToView(viewGroup, content)
}

// Extension for Fragment
fun Fragment.showAsBottomSheet(@IdRes id: Int, content: @Composable (() -> Unit) -> Unit) {
    val viewGroup = requireActivity().findViewById(id) as ViewGroup
    addContentToView(viewGroup, content)
}

private fun addContentToView(
    viewGroup: ViewGroup,
    content: @Composable (() -> Unit) -> Unit,
) {
    viewGroup.addView(
        ComposeView(viewGroup.context).apply {
            setContent {
                BottomSheetWrapper(viewGroup, this, content)
            }
        }
    )
}

@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    content: @Composable (() -> Unit) -> Unit,
) {
    val TAG = parent::class.java.simpleName
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
    MdcTheme {
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

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.fragmentComposeView(
    parent: CompositionContext? = null,
    noinline content: @Composable () -> Unit,
) = ComposeView(requireContext()).apply {
    setParentCompositionContext(parent)
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent {
        SetUp {
            content()
        }
    }
}
