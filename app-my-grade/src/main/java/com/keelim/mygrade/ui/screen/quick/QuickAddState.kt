package com.keelim.mygrade.ui.screen.quick

import androidx.compose.runtime.Stable
import com.keelim.mygrade.ui.screen.main.NormalProbability

@Stable
data class QuickAddState(
    val isError: Boolean,
    val errorMessage: String,
    val normalProbability: NormalProbability,
    val student: Int,
) {
    companion object {
        fun empty() = QuickAddState(
            isError = false,
            errorMessage = "",
            normalProbability = NormalProbability(0),
            student = 0,
        )
    }
}
