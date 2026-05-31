package com.keelim.core.data.source.prompt

import com.google.ai.client.generativeai.GenerativeModel
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class PromptRepositoryImplTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    test("getContent는 응답 text가 비어/null이면 빈 문자열 success로 감싼다") {
        runTest(testDispatcher) {
            val model = mockk<GenerativeModel>(relaxed = true)
            val repository = PromptRepositoryImpl(model, testDispatcher)

            val result = repository.getContent("프롬프트")

            result.isSuccess shouldBe true
            result.getOrNull() shouldBe ""
        }
    }
})
