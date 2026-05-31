package com.keelim.core.data.source

import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class WSRepositoryImplTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    test("disconnect는 활성 세션이 없으면 예외 없이 완료된다") {
        runTest(testDispatcher) {
            val repository = WSRepositoryImpl(mockk(relaxed = true))

            repository.disconnect()
        }
    }
})
