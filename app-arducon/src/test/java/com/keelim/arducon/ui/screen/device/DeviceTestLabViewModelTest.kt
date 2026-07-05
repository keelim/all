package com.keelim.arducon.ui.screen.device

import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceTestLabViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("초기 상태는 모든 테스트를 대기 상태로 만든다") {
        val viewModel = DeviceTestLabViewModel()
        val state = viewModel.uiState.value

        state.totalCount shouldBe DeviceTestId.entries.size
        state.passCount shouldBe 0
        state.failCount shouldBe 0
        state.runningCount shouldBe 0
        state.results.values.all { it.status == DeviceTestStatus.Ready } shouldBe true
    }

    test("테스트 실행과 결과 적용 상태를 기록한다") {
        val viewModel = DeviceTestLabViewModel()

        viewModel.markRunning(DeviceTestId.Camera)
        viewModel.uiState.value.results.getValue(DeviceTestId.Camera).status shouldBe DeviceTestStatus.Running

        viewModel.applyOutcome(
            id = DeviceTestId.Camera,
            outcome = DeviceTestOutcome.pass(DeviceTestMessage.CapabilityAvailable),
        )

        val result = viewModel.uiState.value.results.getValue(DeviceTestId.Camera)
        result.status shouldBe DeviceTestStatus.Pass
        result.message shouldBe DeviceTestMessage.CapabilityAvailable
        viewModel.uiState.value.passCount shouldBe 1
    }

    test("개별 테스트를 초기화한다") {
        val viewModel = DeviceTestLabViewModel()

        viewModel.applyOutcome(
            id = DeviceTestId.Network,
            outcome = DeviceTestOutcome.fail(DeviceTestMessage.NetworkUnavailable),
        )
        viewModel.reset(DeviceTestId.Network)

        val result = viewModel.uiState.value.results.getValue(DeviceTestId.Network)
        result.status shouldBe DeviceTestStatus.Ready
        result.message shouldBe DeviceTestMessage.Ready
        viewModel.uiState.value.failCount shouldBe 0
    }
})
