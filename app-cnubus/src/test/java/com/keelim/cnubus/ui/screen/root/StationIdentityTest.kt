package com.keelim.cnubus.ui.screen.root

import com.keelim.core.data.model.locationList
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StationIdentityTest : FunSpec({
    test("station names used by favorites and lazy keys are unique") {
        val names = locationList.map { it.name }
        names.toSet().size shouldBe names.size
        names.none { it.isBlank() } shouldBe true
    }
})
