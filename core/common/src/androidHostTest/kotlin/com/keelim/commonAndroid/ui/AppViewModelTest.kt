package com.keelim.commonAndroid.ui

import com.keelim.data.repository.FirebaseRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach { Dispatchers.setMain(testDispatcher) }
    afterEach { Dispatchers.resetMain() }

    test("newNotification은 구독 전 초기값으로 빈 문자열을 가진다") {
        val firebaseRepository = mockk<FirebaseRepository>(relaxed = true)

        val viewModel = AppViewModel(testDispatcher, firebaseRepository)

        viewModel.newNotification.value shouldBe ""
    }
})
