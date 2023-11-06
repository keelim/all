package com.keelim.data.model

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize

@Parcelize
data class GradeResult(
    val subject: String,
    val grade: String,
    val point: String,
) : Parcelable {
    companion object {
        fun gradeResultInitial(savedStateHandle: SavedStateHandle): GradeResult {
            return GradeResult(
                subject = checkNotNull(savedStateHandle["subject"]),
                grade = checkNotNull(savedStateHandle["grade"]),
                point = checkNotNull(savedStateHandle["point"]),
            )
        }
    }
}
