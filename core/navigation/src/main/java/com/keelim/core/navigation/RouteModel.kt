package com.keelim.core.navigation

import kotlinx.serialization.Serializable


sealed interface ArduconRoute {
    @Serializable
    data object Main : ArduconRoute
}

sealed interface CnuBusRoute {
    @Serializable
    data object Main : CnuBusRoute

    @Serializable
    data object Map : CnuBusRoute
}
