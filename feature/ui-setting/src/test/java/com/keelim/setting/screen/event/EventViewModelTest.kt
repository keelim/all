package com.keelim.setting.screen.event

import androidx.lifecycle.SavedStateHandle
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EventViewModelTest : FunSpec({
    test("data exposes eventId from SavedStateHandle") {
        val viewModel = EventViewModel(
            savedStateHandle = SavedStateHandle(mapOf("eventId" to 7)),
        )

        viewModel.data.value.eventId shouldBe 7
    }

    test("missing eventId fails fast") {
        shouldThrow<IllegalStateException> {
            EventViewModel(savedStateHandle = SavedStateHandle())
        }
    }
})
