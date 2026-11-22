package com.keelim.cnubus.ui.screen.root

import com.keelim.core.data.model.Location
import com.keelim.core.data.model.locationList
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StationIdentityTest : FunSpec({
    test("all 23 catalog rows retain unique keys despite 21 distinct names") {
        locationList.size shouldBe 23
        locationList.map { it.name }.toSet().size shouldBe 21
        val keys = locationList.map { it.stationRowKey }
        keys.toSet().size shouldBe locationList.size
        keys.none { it.isBlank() } shouldBe true
        for (name in listOf("도서관앞", "골프장")) {
            locationList.count { it.name == name } shouldBe 2
        }
    }

    test("the two library rows remain distinct even with identical names and coordinates") {
        val library = locationList.filter { it.name == "도서관앞" }
        library.map { it.latLng }.distinct().size shouldBe 1
        library.map { it.stationRowKey }.distinct().size shouldBe 2
    }

    test("copies, reordering and search filters preserve catalog row identity") {
        val keys = locationList.map { it.stationRowKey }
        locationList.reversed().map { it.copy().stationRowKey } shouldBe keys.reversed()
        val filtered = locationList.filter { it.name.contains("도서관") }
        val expected = locationList.zip(keys).filter { (station, _) -> station.name.contains("도서관") }.map { it.second }
        filtered.map { it.copy().stationRowKey } shouldBe expected
        filtered.map { it.stationRowKey }.toSet().size shouldBe filtered.size
    }

    test("row keys do not depend on mutable labels or image URLs") {
        val station = locationList.first()
        station.copy(name = "renamed", imgUrl = "changed").stationRowKey shouldBe station.stationRowKey
    }

    test("every route position participates in identity") {
        val station = Location.defaultLocation()
        val variants = listOf(
            station, station.copy(roota = 2), station.copy(rootb = 2),
            station.copy(rootc = 2), station.copy(root_night = 2),
        )
        variants.map { it.stationRowKey }.toSet().size shouldBe variants.size
    }
})
