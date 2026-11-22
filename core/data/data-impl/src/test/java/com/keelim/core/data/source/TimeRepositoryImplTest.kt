package com.keelim.core.data.source

import com.google.android.gms.tasks.Task
import com.google.android.gms.time.TrustedTimeClient
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class TimeRepositoryImplTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()
    val tasksFacade = "kotlinx.coroutines.tasks.TasksKt"

    extension(MainDispatcherRule(testDispatcher))

    // Ensure the static mock never leaks to other specs even if a test fails mid-body.
    afterTest {
        unmockkStatic(tasksFacade)
    }

    test("getCurrentTime은 TrustedTimeClient가 계산한 시간을 반환한다") {
        runTest(testDispatcher) {
            val client = mockk<TrustedTimeClient>()
            every { client.computeCurrentUnixEpochMillis() } returns 1_700_000_000_000L
            val task = mockk<Task<TrustedTimeClient>>()
            mockkStatic(tasksFacade)
            coEvery { task.await() } returns client

            val repository = TimeRepositoryImpl(task, testDispatcher)

            repository.getCurrentTime() shouldBe 1_700_000_000_000L
        }
    }

    test("getCurrentTime은 계산값이 null이면 시스템 시간으로 폴백한다") {
        runTest(testDispatcher) {
            val client = mockk<TrustedTimeClient>()
            every { client.computeCurrentUnixEpochMillis() } returns null
            val task = mockk<Task<TrustedTimeClient>>()
            mockkStatic(tasksFacade)
            coEvery { task.await() } returns client

            val repository = TimeRepositoryImpl(task, testDispatcher)
            val before = System.currentTimeMillis()

            val result = repository.getCurrentTime()

            (result >= before) shouldBe true
        }
    }
})
