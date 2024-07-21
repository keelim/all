package com.keelim.core.navigation

import kotlinx.serialization.Serializable

interface AppRoute

sealed interface ArduconRoute : AppRoute {
    @Serializable
    data object Main : ArduconRoute
}

sealed interface CnuBusRoute : AppRoute {
    @Serializable
    data object Main : CnuBusRoute

    @Serializable
    data object Map : CnuBusRoute
}

sealed interface NandaRoute : AppRoute {
    @Serializable
    data object Main : NandaRoute

    @Serializable
    data object Category : NandaRoute

    @Serializable
    data class Diagnosis(
        val category: String,
        val num: String,
    ) : NandaRoute

    @Serializable
    data class Web(val uri: String) : NandaRoute

    @Serializable
    data object Nutrient : NandaRoute

    @Serializable
    data object NutrientTimer : NandaRoute
}

sealed interface ComssaRoute : AppRoute {
    @Serializable
    data object Calendar : ComssaRoute

    @Serializable
    data object Ecocal : ComssaRoute

    @Serializable
    data object Flash : ComssaRoute
}

sealed interface FeatureRoute : AppRoute {
    @Serializable
    data object Settings : FeatureRoute
}
