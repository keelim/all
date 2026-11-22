package com.keelim.core.data.source.firebase

import android.content.Context
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseRepositoryImplTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    test("getRef는 Firebase 미초기화 등 예외를 Result.failure로 감싸 방출한다") {
        runTest(testDispatcher) {
            val repository = FirebaseRepositoryImpl(mockk<Context>(relaxed = true), testDispatcher)

            val result = repository.getRef("eco").first()

            result.isFailure shouldBe true
        }
    }

    test("getFCMToken은 Firebase 미초기화 등 예외를 Result.failure로 감싸 방출한다") {
        runTest(testDispatcher) {
            val repository = FirebaseRepositoryImpl(mockk<Context>(relaxed = true), testDispatcher)

            val result = repository.getFCMToken().first()

            result.isFailure shouldBe true
        }
    }
})
