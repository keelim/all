package com.keelim.setting.screen.theme

import app.cash.turbine.test
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.data.model.ThemeType
import com.keelim.testing.util.MainDispatcherRule
import dagger.Lazy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeViewModelTest : FunSpec({
    lateinit var viewModel: ThemeViewModel
    lateinit var userStateStore: UserStateStore
    lateinit var lazyUserStateStore: Lazy<UserStateStore>
    lateinit var themeTypeFlow: MutableStateFlow<ThemeType>
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        userStateStore = mockk()
        lazyUserStateStore = mockk()
        themeTypeFlow = MutableStateFlow(ThemeType.LIGHT)

        every { userStateStore.themeTypeFlow } returns themeTypeFlow
        every { userStateStore.setThemeType(any(), any()) } just runs
        every { lazyUserStateStore.get() } returns userStateStore

        viewModel = ThemeViewModel(lazyUserStateStore)
    }

    test("themeTypeState starts with light theme and hidden dialog") {
        viewModel.themeTypeState.value.selectedRadio shouldBe ThemeType.LIGHT
        viewModel.themeTypeState.value.isDialogVisible shouldBe false
    }

    test("setDialogVisibility updates the dialog visibility state") {
        runTest(testDispatcher) {
            viewModel.themeTypeState.test {
                awaitItem().isDialogVisible shouldBe false

                viewModel.setDialogVisibility(true)
                advanceUntilIdle()
                awaitItem().isDialogVisible shouldBe true

                viewModel.setDialogVisibility(false)
                advanceUntilIdle()
                awaitItem().isDialogVisible shouldBe false
            }
        }
    }

    test("themeTypeState reflects upstream theme changes") {
        runTest(testDispatcher) {
            viewModel.themeTypeState.test {
                awaitItem().selectedRadio shouldBe ThemeType.LIGHT

                themeTypeFlow.value = ThemeType.DARK
                advanceUntilIdle()

                awaitItem().selectedRadio shouldBe ThemeType.DARK
            }
        }
    }

    test("updateThemeType delegates to userStateStore") {
        viewModel.updateThemeType(ThemeType.DARK)

        verify { userStateStore.setThemeType(ThemeType.DARK, any()) }
    }
})
