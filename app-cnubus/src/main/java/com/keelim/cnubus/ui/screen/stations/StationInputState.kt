package com.keelim.cnubus.ui.screen.stations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class StationInputState(
    private val hint: String,
    initialText: String,
) {
    var text by mutableStateOf(initialText)

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<StationInputState, *> = listSaver(
            save = { listOf(it.hint, it.hint) },
            restore = {
                StationInputState(
                    hint = it[0],
                    initialText = it[1]
                )
            }
        )
    }
}

@Composable
fun rememberStationInputState(hint: String): StationInputState =
    rememberSaveable(hint, saver = StationInputState.Saver) {
        StationInputState(hint, hint)
    }
