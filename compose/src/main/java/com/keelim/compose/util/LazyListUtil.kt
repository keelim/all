package com.keelim.compose.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf

val LazyListState.isScrolled: Boolean
    get() = derivedStateOf { firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0 }.value
