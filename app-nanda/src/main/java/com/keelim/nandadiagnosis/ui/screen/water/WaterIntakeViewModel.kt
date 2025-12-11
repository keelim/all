package com.keelim.nandadiagnosis.ui.screen.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.model.DailyWaterTotal
import com.keelim.shared.data.database.dao.WaterIntakeDao
import com.keelim.shared.data.database.model.WaterIntake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class WaterIntakeUiModel(
    val id: Long,
    val amount: Int,
    val formattedTime: String,
)

@HiltViewModel
class WaterIntakeViewModel @Inject constructor(
    private val waterIntakeDao: WaterIntakeDao,
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val today: String get() = LocalDate.now().format(dateFormatter)

    // 일일 목표 (ml) - 기본값 2000ml
    private val _dailyGoal = MutableStateFlow(2000)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    // 오늘 섭취한 총량
    val todayTotal: StateFlow<Int> = waterIntakeDao.getTotalByDate(today)
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0,
        )

    // 오늘 섭취 기록 리스트 (UI 모델로 변환)
    val todayRecords: StateFlow<List<WaterIntakeUiModel>> = waterIntakeDao.getByDate(today)
        .map { records ->
            records.map { record ->
                WaterIntakeUiModel(
                    id = record.id,
                    amount = record.amount,
                    formattedTime = timeFormatter.format(Date(record.timestamp)),
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    // 최근 7일 일별 총량
    val weeklyHistory: StateFlow<List<DailyWaterTotal>> = waterIntakeDao.getDailyTotals(7)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun addWaterIntake(amount: Int) {
        viewModelScope.launch {
            waterIntakeDao.insert(
                WaterIntake(
                    amount = amount,
                    timestamp = System.currentTimeMillis(),
                    date = today,
                ),
            )
        }
    }

    fun deleteWaterIntake(id: Long) {
        viewModelScope.launch {
            waterIntakeDao.deleteById(id)
        }
    }

    fun setDailyGoal(goal: Int) {
        _dailyGoal.value = goal
    }
}

