package com.keelim.common.extensions.compose

import android.content.pm.ApplicationInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

class CompositionCounter(var count: Int)

@Composable
fun LogComposable(tag: String = "log", message: String) {
    if (LocalContext.current.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == 0) return
    val compositionCounter: CompositionCounter = remember { CompositionCounter(0) }
    Timber.tag(tag).d(message + " " + compositionCounter.count + " " + currentRecomposeScope)
    compositionCounter.count++
}
