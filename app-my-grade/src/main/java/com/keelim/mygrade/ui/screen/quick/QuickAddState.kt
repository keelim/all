package com.keelim.mygrade.ui.screen.quick

import androidx.compose.runtime.Stable
import com.keelim.mygrade.ui.screen.main.NormalProbability

@Stable
data class QuickAddState(
    val isError: Boolean,
    val errorMessage: String,
    val subject: String,
    val normalProbability: NormalProbability,
    val student: Int,
) {
    fun isValid() : Boolean = subject.isNotEmpty() && normalProbability.value != 0 && student != 0
    companion object {
        fun empty() = QuickAddState(
            isError = false,
            errorMessage = "",
            subject = "",
            normalProbability = NormalProbability(0),
            student = 0,
        )
    }
}
