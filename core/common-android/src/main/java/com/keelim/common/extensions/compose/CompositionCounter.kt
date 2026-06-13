package com.keelim.common.extensions.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import com.keelim.common.BuildConfig
import timber.log.Timber

class CompositionCounter(var count: Int)

@Composable
fun LogComposable(tag: String = "log", message: String) {
    if (BuildConfig.DEBUG.not()) return
    val compositionCounter: CompositionCounter = remember { CompositionCounter(0) }
    Timber.tag(tag).d(message + " " + compositionCounter.count + " " + currentRecomposeScope)
    compositionCounter.count++
}
