package com.keelim.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: KeelimDispatchers)

enum class KeelimDispatchers {
    IO,
    DEFAULT,
    MAIN,
    MAIN_IMMEDIATE,
    UNCONFINED,
}
