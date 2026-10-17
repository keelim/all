package com.keelim.appfunction.health

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class HealthAppFunctionsTest : FunSpec({
    test("health status provider returns stable aggregate metadata") {
        val result = HealthStatusProvider(clock = HealthStatusClock { 1234L }).status()

        result.status shouldBe "ok"
        result.module shouldBe "feature:app-function"
        result.epochMillis shouldBe 1234L
    }

    test("health status provider returns current status and module") {
        runTest {
            val result = HealthStatusProvider().status()

            result.status shouldBe "ok"
            result.module shouldBe "feature:app-function"
            (result.epochMillis > 0L) shouldBe true
        }
    }
})
