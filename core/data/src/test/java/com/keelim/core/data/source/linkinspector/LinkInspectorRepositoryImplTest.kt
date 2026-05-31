package com.keelim.core.data.source.linkinspector

import android.content.Context
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class LinkInspectorRepositoryImplTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    fun repositoryWithRealClient(): LinkInspectorRepositoryImpl =
        LinkInspectorRepositoryImpl(mockk<Context>(relaxed = true), HttpClient(CIO))

    test("checkHttp는 잘못된 URL이면 runCatching으로 null을 반환한다") {
        runTest(testDispatcher) {
            val repository = repositoryWithRealClient()

            repository.checkHttp("invalid://broken/url") shouldBe null
        }
    }

    test("fetchOg는 잘못된 URL이면 runCatching으로 null을 반환한다") {
        runTest(testDispatcher) {
            val repository = repositoryWithRealClient()

            repository.fetchOg("invalid://broken/url") shouldBe null
        }
    }
})
