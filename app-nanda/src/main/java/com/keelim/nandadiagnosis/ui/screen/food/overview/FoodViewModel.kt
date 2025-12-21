package com.keelim.nandadiagnosis.ui.screen.food.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.shared.data.database.dao.FoodDao
import com.keelim.shared.data.database.model.FoodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import jakarta.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodDao: FoodDao,
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val today: String get() = LocalDate.now().format(dateFormatter)

    val todayFoods: StateFlow<List<FoodEntity>> = foodDao.getByDate(today)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val todayTotalCalories: StateFlow<Int> = foodDao.getTotalCaloriesByDate(today)
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0,
        )

    fun addFood(title: String, calories: String) {
        val caloriesInt = calories.toIntOrNull() ?: return
        if (title.isBlank()) return

        viewModelScope.launch {
            foodDao.insert(
                FoodEntity(
                    title = title,
                    calories = caloriesInt,
                    date = today,
                    time = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteFood(id: Long) {
        viewModelScope.launch {
            foodDao.deleteById(id)
        }
    }
}
