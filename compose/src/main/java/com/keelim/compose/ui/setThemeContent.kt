package com.keelim.compose.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme

@Suppress("NOTHING_TO_INLINE")
inline fun ComponentActivity.setThemeContent(
    noinline content: @Composable () -> Unit
) = setContent {
    MdcTheme {
        content()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.setThemeContent(
    noinline content: @Composable () -> Unit
) = ComposeView(requireContext()).apply {
    setContent {
        MdcTheme {
            content()
        }
    }
}
