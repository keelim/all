package com.keelim.cnubus.ui.screen.root

import app.cash.turbine.test
import com.keelim.core.data.model.Location
import com.keelim.core.data.model.locationList
import com.keelim.data.repository.StationRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class RootViewModelTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    fun expectedRouteA(): List<Location> = locationList.filter { it.roota != Location.EX_NUMBER }.sortedBy { it.roota }

    fun expectedRouteB(): List<Location> = locationList.filter { it.rootb != Location.EX_NUMBER }.sortedBy { it.rootb }

    fun expectedRouteC(): List<Location> = locationList.filter { it.rootc != Location.EX_NUMBER }.sortedBy { it.rootc }

    fun expectedNightRoute(): List<Location> =
        locationList.filter { it.root_night != Location.EX_NUMBER }.sortedBy { it.root_night }

    test("초기 상태는 A 노선 목록을 노출하고 data 도 함께 갱신해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem() shouldBe MapEvent.UnInitialized
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess

                success.data shouldBe expectedRouteA()
                viewModel.data.value shouldBe expectedRouteA()
            }
        }
    }

    test("B 모드로 변경하면 rootb 기준 정렬 목록을 노출해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("b")
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess
                success.data shouldBe expectedRouteB()
            }
        }
    }

    test("C 모드로 변경하면 rootc 기준 정렬 목록을 노출해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("c")
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess
                success.data shouldBe expectedRouteC()
            }
        }
    }

    test("야간 모드로 변경하면 root_night 기준 정렬 목록을 노출해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("d")
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess
                success.data shouldBe expectedNightRoute()
            }
        }
    }

    test("즐겨찾기 모드는 저장소 즐겨찾기와 일치하는 정류장만 노출해야 한다") {
        runTest {
            val repository = FakeStationRepository(setOf("도서관앞"))
            val viewModel = RootViewModel(repository)

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("f")
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess
                success.data shouldBe locationList.filter { repository.favoriteStationsFlow.value.contains(it.name) }
            }
        }
    }

    test("검색 모드는 빈 검색어면 비우고 검색어가 있으면 이름 포함 항목만 노출해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("s")
                advanceUntilIdle()

                val emptyResult = awaitItem() as MapEvent.MigrateSuccess
                emptyResult.data shouldBe emptyList()

                viewModel.updateQuery("도서관")
                viewModel.query.value shouldBe "도서관"
                advanceUntilIdle()

                val queryResult = awaitItem() as MapEvent.MigrateSuccess
                queryResult.data shouldBe locationList.filter { it.name.contains("도서관") }
            }
        }
    }

    test("알 수 없는 모드는 빈 목록을 노출해야 한다") {
        runTest {
            val viewModel = RootViewModel(FakeStationRepository())

            viewModel.state.test {
                awaitItem()
                advanceUntilIdle()
                awaitItem()

                viewModel.setMode("x")
                advanceUntilIdle()

                val success = awaitItem() as MapEvent.MigrateSuccess
                success.data shouldBe emptyList()
            }
        }
    }

    test("toggleFavorite 는 즐겨찾기에 없으면 추가해야 한다") {
        runTest {
            val repository = FakeStationRepository()
            val viewModel = RootViewModel(repository)
            val stationName = "정심화국제문화회관"

            viewModel.toggleFavorite(stationName)
            advanceUntilIdle()

            repository.addedStations shouldBe listOf(stationName)
            repository.favoriteStationsFlow.value shouldBe setOf(stationName)
        }
    }

    test("toggleFavorite 는 이미 즐겨찾기면 제거해야 한다") {
        runTest {
            val stationName = "도서관앞"
            val repository = FakeStationRepository(setOf(stationName))
            val viewModel = RootViewModel(repository)

            viewModel.favorites.test {
                awaitItem() shouldBe emptySet()
                advanceUntilIdle()
                awaitItem() shouldBe setOf(stationName)

                viewModel.toggleFavorite(stationName)
                advanceUntilIdle()

                repository.removedStations shouldBe listOf(stationName)
                awaitItem() shouldBe emptySet()
            }
        }
    }
})

private class FakeStationRepository(
    initialFavorites: Set<String> = emptySet(),
) : StationRepository {
    val favoriteStationsFlow = MutableStateFlow(initialFavorites)
    val addedStations = mutableListOf<String>()
    val removedStations = mutableListOf<String>()

    override val favoriteStations = favoriteStationsFlow

    override suspend fun addFavorite(stationName: String) {
        addedStations += stationName
        favoriteStationsFlow.value = favoriteStationsFlow.value + stationName
    }

    override suspend fun removeFavorite(stationName: String) {
        removedStations += stationName
        favoriteStationsFlow.value = favoriteStationsFlow.value - stationName
    }
}
