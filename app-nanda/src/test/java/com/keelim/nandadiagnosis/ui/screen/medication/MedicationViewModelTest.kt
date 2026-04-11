package com.keelim.nandadiagnosis.ui.screen.medication

import app.cash.turbine.test
import com.keelim.data.model.Medication
import com.keelim.data.model.MedicationFrequency
import com.keelim.data.repository.MedicationRepository
import com.keelim.nandadiagnosis.notification.MedicationNotificationManager
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MedicationViewModelTest : FunSpec({
    lateinit var repository: MedicationRepository
    lateinit var notificationManager: MedicationNotificationManager
    lateinit var medicationsFlow: MutableStateFlow<List<Medication>>
    lateinit var viewModel: MedicationViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        repository = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)
        medicationsFlow = MutableStateFlow(emptyList())
        every { repository.getMedications() } returns medicationsFlow
        viewModel = MedicationViewModel(repository, notificationManager)
    }

    test("showAddDialog opens a blank dialog") {
        val existing = medication(id = "edit-id")

        viewModel.showEditDialog(existing)
        viewModel.showAddDialog()

        viewModel.showAddDialog.value shouldBe true
        viewModel.editingMedication.value shouldBe null
    }

    test("showEditDialog exposes the selected medication") {
        val existing = medication(id = "edit-id", name = "혈압약")

        viewModel.showEditDialog(existing)

        viewModel.showAddDialog.value shouldBe true
        viewModel.editingMedication.value shouldBe existing
    }

    test("hideAddDialog clears dialog state") {
        viewModel.showEditDialog(medication())

        viewModel.hideAddDialog()

        viewModel.showAddDialog.value shouldBe false
        viewModel.editingMedication.value shouldBe null
    }

    test("toggleMedication disables an enabled medication and cancels notifications") {
        runTest(testDispatcher) {
            val existing = medication(id = "med-1", isEnabled = true)
            coEvery { repository.updateMedication(any()) } returns Unit

            viewModel.toggleMedication(existing)
            advanceUntilIdle()

            coVerify {
                repository.updateMedication(
                    match { it.id == "med-1" && !it.isEnabled },
                )
            }
            verify {
                notificationManager.cancelNotification(
                    match { it.id == "med-1" && !it.isEnabled },
                )
            }
        }
    }

    test("toggleMedication enables a disabled medication and schedules notifications") {
        runTest(testDispatcher) {
            val existing = medication(id = "med-2", isEnabled = false)
            coEvery { repository.updateMedication(any()) } returns Unit

            viewModel.toggleMedication(existing)
            advanceUntilIdle()

            coVerify {
                repository.updateMedication(
                    match { it.id == "med-2" && it.isEnabled },
                )
            }
            verify {
                notificationManager.scheduleNotification(
                    match { it.id == "med-2" && it.isEnabled },
                )
            }
        }
    }

    test("addMedication stores a new enabled medication, schedules it, and closes the dialog") {
        runTest(testDispatcher) {
            coEvery { repository.addMedication(any()) } returns Unit

            viewModel.showAddDialog()
            viewModel.addMedication(
                name = "아스피린",
                dosage = "2정",
                hour = 9,
                minute = 15,
                frequency = MedicationFrequency.SPECIFIC_DAYS,
                notes = "식후 복용",
            )
            advanceUntilIdle()

            coVerify {
                repository.addMedication(
                    match {
                        it.name == "아스피린" &&
                            it.dosage == "2정" &&
                            it.hour == 9 &&
                            it.minute == 15 &&
                            it.isEnabled &&
                            it.frequency == MedicationFrequency.SPECIFIC_DAYS &&
                            it.notes == "식후 복용" &&
                            it.id.isNotBlank()
                    },
                )
            }
            verify {
                notificationManager.scheduleNotification(
                    match {
                        it.name == "아스피린" &&
                            it.dosage == "2정" &&
                            it.hour == 9 &&
                            it.minute == 15
                    },
                )
            }
            viewModel.showAddDialog.value shouldBe false
            viewModel.editingMedication.value shouldBe null
        }
    }

    test("updateMedication does nothing when no item is being edited") {
        runTest(testDispatcher) {
            viewModel.updateMedication(
                name = "새 약",
                dosage = "1정",
                hour = 7,
                minute = 45,
            )
            advanceUntilIdle()

            coVerify(exactly = 0) { repository.updateMedication(any()) }
            verify(exactly = 0) { notificationManager.scheduleNotification(any()) }
        }
    }

    test("updateMedication saves the edited medication, reschedules it, and closes the dialog") {
        runTest(testDispatcher) {
            val existing = medication(id = "med-3", name = "기존 약", dosage = "1정")
            coEvery { repository.updateMedication(any()) } returns Unit

            viewModel.showEditDialog(existing)
            viewModel.updateMedication(
                name = "새 약",
                dosage = "2정",
                hour = 10,
                minute = 20,
            )
            advanceUntilIdle()

            coVerify {
                repository.updateMedication(
                    match {
                        it.id == "med-3" &&
                            it.name == "새 약" &&
                            it.dosage == "2정" &&
                            it.hour == 10 &&
                            it.minute == 20 &&
                            it.isEnabled
                    },
                )
            }
            verify {
                notificationManager.scheduleNotification(
                    match {
                        it.id == "med-3" &&
                            it.name == "새 약" &&
                            it.dosage == "2정" &&
                            it.hour == 10 &&
                            it.minute == 20
                    },
                )
            }
            viewModel.showAddDialog.value shouldBe false
            viewModel.editingMedication.value shouldBe null
        }
    }

    test("removeMedication cancels notification and deletes the record") {
        runTest(testDispatcher) {
            val existing = medication(id = "med-4")
            coEvery { repository.removeMedication("med-4") } returns Unit

            viewModel.removeMedication(existing)
            advanceUntilIdle()

            verify { notificationManager.cancelNotification(existing) }
            coVerify { repository.removeMedication("med-4") }
        }
    }

    test("rescheduleAllEnabled only schedules enabled medications") {
        runTest(testDispatcher) {
            val enabled = medication(id = "enabled", isEnabled = true)
            val disabled = medication(id = "disabled", isEnabled = false)
            medicationsFlow.value = listOf(enabled, disabled)

            viewModel.medications.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()

                awaitItem() shouldBe listOf(enabled, disabled)

                viewModel.rescheduleAllEnabled()
                advanceUntilIdle()

                verify(exactly = 1) { notificationManager.scheduleNotification(enabled) }
                verify(exactly = 0) { notificationManager.scheduleNotification(disabled) }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}) {
    companion object {
        private fun medication(
            id: String = "medication-id",
            name: String = "영양제",
            dosage: String = "1정",
            hour: Int = 8,
            minute: Int = 30,
            isEnabled: Boolean = true,
            frequency: MedicationFrequency = MedicationFrequency.DAILY,
            notes: String = "",
        ) = Medication(
            id = id,
            name = name,
            dosage = dosage,
            hour = hour,
            minute = minute,
            isEnabled = isEnabled,
            frequency = frequency,
            notes = notes,
        )
    }
}
