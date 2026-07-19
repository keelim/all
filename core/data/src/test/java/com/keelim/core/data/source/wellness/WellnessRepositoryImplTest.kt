package com.keelim.core.data.source.wellness

import android.content.Context
import android.content.SharedPreferences
import com.keelim.core.database.wellness.WellnessDao
import com.keelim.model.wellness.WellnessPreferences
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class WellnessRepositoryImplTest : FunSpec({
    test("onboarding preference keeps the existing storage contract") {
        runTest {
            var onboardingAccepted = false
            val editor = mockk<SharedPreferences.Editor>()
            val sharedPreferences = mockk<SharedPreferences>()
            every {
                sharedPreferences.getBoolean("wellness_onboarding_accepted", false)
            } answers { onboardingAccepted }
            every { sharedPreferences.edit() } returns editor
            every { editor.putBoolean("wellness_onboarding_accepted", any()) } answers {
                onboardingAccepted = secondArg<Boolean>()
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
            val repository =
                WellnessRepositoryImpl(
                    dao = dao,
                    context = context,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )

            repository.preferencesSnapshot() shouldBe WellnessPreferences()

            repository.setOnboardingAccepted(true)

            repository.preferencesSnapshot() shouldBe
                WellnessPreferences(onboardingAccepted = true)
        }
    }
})
