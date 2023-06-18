package com.keelim.mygrade.ui.screen.center.main

import androidx.compose.runtime.Stable


@Stable
data class QuickAddState(
    val isError: Boolean,
    val errorMessage: String,
    val normalProbability: NormalProbability,
    val student: Int
) {
    companion object {
        fun empty() = QuickAddState(
            isError = false,
            errorMessage = "",
            normalProbability = NormalProbability(0),
            student = 0
        )
    }
}
