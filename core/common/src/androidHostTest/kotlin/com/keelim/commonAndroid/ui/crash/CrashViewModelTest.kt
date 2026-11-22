package com.keelim.commonAndroid.ui.crash

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe

class CrashViewModelTest : FunSpec({

    test("CrashViewModel은 의존성 없이 생성된다") {
        CrashViewModel() shouldNotBe null
    }
})
