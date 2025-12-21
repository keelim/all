package com.keelim.nandadiagnosis.ui.screen.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.shared.data.database.dao.ExerciseDao
import com.keelim.shared.data.database.model.ExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import jakarta.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao,
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val today: String get() = LocalDate.now().format(dateFormatter)

    val todayExercises: StateFlow<List<ExerciseEntity>> = exerciseDao.getByDate(today)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun addExercise(title: String, duration: String) {
        if (title.isBlank() || duration.isBlank()) return

        viewModelScope.launch {
            exerciseDao.insert(
                ExerciseEntity(
                    title = title,
                    duration = duration,
                    date = today,
                    time = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExercise(id: Long) {
        viewModelScope.launch {
            exerciseDao.deleteById(id)
        }
    }
}
