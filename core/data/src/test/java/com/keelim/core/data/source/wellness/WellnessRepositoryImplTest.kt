package com.keelim.core.data.source.wellness

import android.content.Context
import android.content.SharedPreferences
import com.keelim.core.database.wellness.WellnessDao
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class WellnessRepositoryImplTest : FunSpec({
    test("default routines initialize only for a new install") {
        runTest {
            var legacyOnboardingAccepted = false
            var defaultsInitialized = false
            val editor = mockk<SharedPreferences.Editor>()
            val sharedPreferences = mockk<SharedPreferences>()
            every {
                sharedPreferences.getBoolean("wellness_onboarding_accepted", false)
            } answers { legacyOnboardingAccepted }
            every {
                sharedPreferences.getBoolean("wellness_defaults_initialized", false)
            } answers { defaultsInitialized }
            every { sharedPreferences.edit() } returns editor
            every { editor.putBoolean("wellness_defaults_initialized", any()) } answers {
                defaultsInitialized = secondArg<Boolean>()
                editor
            }
            every { editor.apply() } just Runs

            val context = mockk<Context>()
            every {
                context.getSharedPreferences(
                    "wellness_service_preferences",
                    Context.MODE_PRIVATE,
                )
            } returns sharedPreferences
            val dao = mockk<WellnessDao>()
            every { dao.observeMeasurements() } returns flowOf(emptyList())
            every { dao.observeRoutines() } returns flowOf(emptyList())
            every { dao.observeRoutineCompletions() } returns flowOf(emptyList())
            every { dao.observeGoal() } returns flowOf(null)
            coEvery { dao.insertRoutines(any()) } just Runs
            val repository =
                WellnessRepositoryImpl(
                    dao = dao,
                    context = context,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )

            repository.initializeDefaultRoutines("2026-07-19")
            repository.initializeDefaultRoutines("2026-07-19")

            io.mockk.coVerify(exactly = 1) { dao.insertRoutines(any()) }
        }
    }
})
