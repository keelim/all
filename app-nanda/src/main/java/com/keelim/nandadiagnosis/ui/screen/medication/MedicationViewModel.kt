package com.keelim.nandadiagnosis.ui.screen.medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.Medication
import com.keelim.data.model.MedicationFrequency
import com.keelim.data.repository.MedicationRepository
import com.keelim.nandadiagnosis.notification.MedicationNotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import jakarta.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository,
    private val notificationManager: MedicationNotificationManager
) : ViewModel() {

    val medications: StateFlow<List<Medication>> = repository.getMedications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog

    private val _editingMedication = MutableStateFlow<Medication?>(null)
    val editingMedication: StateFlow<Medication?> = _editingMedication

    fun toggleMedication(medication: Medication) {
        viewModelScope.launch {
            val updated = medication.copy(isEnabled = !medication.isEnabled)
            repository.updateMedication(updated)

            if (updated.isEnabled) {
                notificationManager.scheduleNotification(updated)
            } else {
                notificationManager.cancelNotification(updated)
            }
        }
    }

    fun addMedication(
        name: String,
        dosage: String,
        hour: Int,
        minute: Int,
        frequency: MedicationFrequency = MedicationFrequency.DAILY,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val medication = Medication(
                id = UUID.randomUUID().toString(),
                name = name,
                dosage = dosage,
                hour = hour,
                minute = minute,
                isEnabled = true,
                frequency = frequency,
                notes = notes
            )
            repository.addMedication(medication)
            notificationManager.scheduleNotification(medication)
            hideAddDialog()
        }
    }

    fun updateMedication(
        name: String,
        dosage: String,
        hour: Int,
        minute: Int
    ) {
        val editing = _editingMedication.value ?: return
        viewModelScope.launch {
            val updated = editing.copy(
                name = name,
                dosage = dosage,
                hour = hour,
                minute = minute
            )
            repository.updateMedication(updated)
            if (updated.isEnabled) {
                notificationManager.scheduleNotification(updated)
            }
            hideAddDialog()
        }
    }

    fun removeMedication(medication: Medication) {
        viewModelScope.launch {
            notificationManager.cancelNotification(medication)
            repository.removeMedication(medication.id)
        }
    }

    fun showAddDialog() {
        _editingMedication.value = null
        _showAddDialog.value = true
    }

    fun showEditDialog(medication: Medication) {
        _editingMedication.value = medication
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
        _editingMedication.value = null
    }

    fun rescheduleAllEnabled() {
        viewModelScope.launch {
            medications.value.filter { it.isEnabled }.forEach { medication ->
                notificationManager.scheduleNotification(medication)
            }
        }
    }
}
