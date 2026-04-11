package com.keelim.appfunction.health

import android.content.Context
import androidx.appfunctions.AppFunctionContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class HealthAppFunctionsTest : FunSpec({
    test("health app function returns stable status and module") {
        runTest {
            val appFunctionContext = object : AppFunctionContext {
                override val context: Context = mockk(relaxed = true)
            }

            val result = HealthAppFunctions().getHealthStatus(appFunctionContext)

            result.status shouldBe "ok"
            result.module shouldBe "feature:app-function"
            (result.epochMillis > 0L) shouldBe true
        }
    }
})
