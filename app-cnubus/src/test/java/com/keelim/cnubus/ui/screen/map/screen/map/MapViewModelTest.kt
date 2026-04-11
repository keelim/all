package com.keelim.cnubus.ui.screen.map.screen.map

import app.cash.turbine.test
import com.keelim.core.data.model.locationList
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("locations 는 초기 빈 상태 후 정적 위치 목록을 MapState 로 매핑해야 한다") {
        runTest {
            val viewModel = MapViewModel()

            viewModel.locations.test {
                awaitItem() shouldBe persistentListOf()
                advanceUntilIdle()

                val locations = awaitItem()
                val firstLocation = locationList.first()
                val firstMapState = locations.first()

                locations.size shouldBe locationList.size
                firstMapState.name shouldBe firstLocation.name
                firstMapState.latlng shouldBe firstLocation.latLng
                firstMapState.itemSnippet shouldBe firstLocation.name
                firstMapState.imageUrl shouldBe firstLocation.imgUrl
            }
        }
    }
})
