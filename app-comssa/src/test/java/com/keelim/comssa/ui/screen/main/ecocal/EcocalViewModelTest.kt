package com.keelim.comssa.ui.screen.main.ecocal

import app.cash.turbine.test
import app.cash.turbine.ReceiveTurbine
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.data.repository.FirebaseRepository
import com.keelim.model.EcoCalEntry
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class EcocalViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("items apply priority and country filters") {
        runTest {
            val repository = FakeFirebaseRepository(
                Result.success(
                    listOf(
                        EcoCalEntry(
                            country = "KR",
                            date = "2026-03-08",
                            priority = "상",
                            time = "09:00",
                            title = "Korea CPI",
                        ),
                        EcoCalEntry(
                            country = "US",
                            date = "2026-03-08",
                            priority = "중",
                            time = "11:00",
                            title = "US Jobs",
                        ),
                        EcoCalEntry(
                            country = "KR",
                            date = "2026-03-09",
                            priority = "하",
                            time = "08:00",
                            title = "Korea Exports",
                        ),
                        EcoCalEntry(
                            country = "US",
                            date = "2026-03-09",
                            priority = "하",
                            time = "12:00",
                            title = "US Bonds",
                        ),
                    ),
                ),
            )
            val viewModel = EcocalViewModel(repository)
            viewModel.items.test {
                awaitItem() shouldBe SealedUiState.loading()
                advanceUntilIdle()

                val initialState = awaitSuccessWhere { state ->
                    state.containsKey("2026-03-08") && state.containsKey("2026-03-09")
                }
                initialState.value shouldContainKey "2026-03-08"
                initialState.value shouldContainKey "2026-03-09"

                viewModel.updateFilter(High())
                advanceUntilIdle()

                val highState = awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title } == listOf("Korea CPI")
                }
                highState.value.shouldContainKey("2026-03-08")
                highState.value.requireEntries("2026-03-08").map { it.title } shouldContainExactly listOf("Korea CPI")

                viewModel.updateFilter(Medium())
                advanceUntilIdle()

                val mediumState = awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title } == listOf("US Jobs")
                }
                mediumState.value.requireEntries("2026-03-08").map { it.title } shouldContainExactly listOf("US Jobs")

                viewModel.updateFilter(Low())
                advanceUntilIdle()

                awaitSuccessWhere { state ->
                    state["2026-03-09"]?.map { it.title }?.toSet() == setOf("Korea Exports", "US Bonds")
                }

                viewModel.updateCountry("KR")
                advanceUntilIdle()

                val lowState = awaitSuccessWhere { state ->
                    state["2026-03-09"]?.map { it.title } == listOf("Korea Exports")
                }
                lowState.value.keys.toList() shouldContainExactly listOf("2026-03-09")
                lowState.value.requireEntries("2026-03-09").map { it.title } shouldContainExactly listOf("Korea Exports")

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }

    test("clear filter resets selected country") {
        runTest {
            val repository = FakeFirebaseRepository(
                Result.success(
                    listOf(
                        EcoCalEntry(country = "KR", date = "2026-03-08", priority = "상", time = "09:00", title = "Korea CPI"),
                        EcoCalEntry(country = "KR", date = "2026-03-08", priority = "하", time = "10:00", title = "Korea Exports"),
                        EcoCalEntry(country = "US", date = "2026-03-08", priority = "상", time = "11:00", title = "US CPI"),
                    ),
                ),
            )
            val viewModel = EcocalViewModel(repository)
            viewModel.items.test {
                awaitItem() shouldBe SealedUiState.loading()
                advanceUntilIdle()
                awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title }?.toSet() ==
                        setOf("Korea CPI", "Korea Exports", "US CPI")
                }

                viewModel.updateCountry("KR")
                advanceUntilIdle()
                val countryState = awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title } == listOf("Korea CPI", "Korea Exports")
                }
                countryState.value.requireEntries("2026-03-08").map { it.title } shouldContainExactly listOf(
                    "Korea CPI",
                    "Korea Exports",
                )

                viewModel.updateFilter(High())
                advanceUntilIdle()

                val filteredState = awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title } == listOf("Korea CPI")
                }
                filteredState.value.requireEntries("2026-03-08").map { it.title } shouldContainExactly listOf("Korea CPI")

                viewModel.updateFilter(Clear())
                advanceUntilIdle()

                val clearedState = awaitSuccessWhere { state ->
                    state["2026-03-08"]?.map { it.title }?.toSet() ==
                        setOf("Korea CPI", "Korea Exports", "US CPI")
                }
                clearedState.value.requireEntries("2026-03-08").map { it.title } shouldContainExactly listOf(
                    "Korea CPI",
                    "Korea Exports",
                    "US CPI",
                )

                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }

    test("repository failures keep items in loading state") {
        runTest {
            val repository = FakeFirebaseRepository(Result.failure(IllegalStateException("boom")))
            val viewModel = EcocalViewModel(repository)

            viewModel.items.test {
                awaitItem() shouldBe SealedUiState.loading()
                advanceUntilIdle()
                val state = viewModel.items.value
                (
                    state == SealedUiState.loading<Map<String, List<EcoCalModel>>>() ||
                        (state is SealedUiState.Success && state.value.isEmpty())
                    ) shouldBe true
                cancelAndIgnoreRemainingEvents()
            }
            viewModel.viewModelScope.cancel()
        }
    }
})

private class FakeFirebaseRepository(
    initialResult: Result<List<EcoCalEntry>>,
) : FirebaseRepository {
    private val refFlow = MutableStateFlow(initialResult)

    override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = refFlow

    override fun getFCMToken(): Flow<Result<String>> = flowOf(Result.success(""))

    override suspend fun getValue(key: String): String = ""
}

private suspend fun ReceiveTurbine<SealedUiState<Map<String, List<EcoCalModel>>>>.awaitSuccessWhere(
    predicate: (Map<String, List<EcoCalModel>>) -> Boolean,
):
    SealedUiState.Success<Map<String, List<EcoCalModel>>> {
    while (true) {
        when (val item = awaitItem()) {
            is SealedUiState.Success -> {
                if (predicate(item.value)) {
                    return item
                }
            }
            else -> Unit
        }
    }
}

private fun Map<String, List<EcoCalModel>>.requireEntries(
    key: String,
): List<EcoCalModel> = getValue(key)
