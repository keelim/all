package com.keelim.appfunction.health

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], manifest = Config.NONE)
class HealthAppFunctionServiceTest {
    private lateinit var controller: ServiceController<HealthAppFunctionService>

    @Before
    fun createService() {
        controller = Robolectric.buildService(HealthAppFunctionService::class.java).create()
    }

    @After
    fun destroyService() {
        controller.destroy()
    }

    @Test
    fun preservesExternalFunctionIdentifier() {
        HealthAppFunctionService.FUNCTION_ID_GET_HEALTH_STATUS shouldBe
            "com.keelim.appfunction.health.HealthAppFunctions#getHealthStatus"
    }

    @Test
    fun generatedServiceReturnsStableStatusAndModule() = runTest {
        val result = controller.get().getHealthStatus()
        result.status shouldBe "ok"
        result.module shouldBe "feature:app-function"
        (result.epochMillis > 0L) shouldBe true
    }
}
