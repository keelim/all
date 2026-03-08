package com.keelim.setting.screen.maintenance

import app.cash.turbine.test
import com.keelim.domain.MaintenanceChecker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

class MaintenanceViewModelTest : FunSpec({
    test("isUnderMaintenance exposes the checker state unchanged") {
        val maintenanceFlow = MutableStateFlow(false)
        val maintenanceChecker = object : MaintenanceChecker {
            override fun initialize() = Unit
            override val isUnderMaintenance = maintenanceFlow
        }
        val viewModel = MaintenanceViewModel(maintenanceChecker)

        runTest {
            viewModel.isUnderMaintenance.test {
                awaitItem() shouldBe false

                maintenanceFlow.value = true
                awaitItem() shouldBe true
            }
        }
    }
})
