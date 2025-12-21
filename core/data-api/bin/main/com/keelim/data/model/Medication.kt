package com.keelim.data.model

import kotlinx.serialization.Serializable

/**
 * Medication reminder data model for medication tracking and notifications.
 */
@Serializable
data class Medication(
    val id: String,
    val name: String,
    val dosage: String,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val frequency: MedicationFrequency = MedicationFrequency.DAILY,
    val daysOfWeek: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7), // 1 = Monday, 7 = Sunday
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
@Serializable
enum class MedicationFrequency {
    DAILY,
    EVERY_OTHER_DAY,
    SPECIFIC_DAYS
}
