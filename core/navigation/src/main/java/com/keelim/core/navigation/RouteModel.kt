package com.keelim.core.navigation

import kotlinx.serialization.Serializable


sealed interface ArduconRoute {
    @Serializable
    data object Main: ArduconRoute
}
