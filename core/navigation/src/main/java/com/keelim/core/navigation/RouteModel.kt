package com.keelim.core.navigation

import kotlinx.serialization.Serializable

interface AppRoute

sealed interface SaastatusRoute : AppRoute {
    @Serializable
    data object Main : SaastatusRoute

    @Serializable
    data object Search : SaastatusRoute
}

sealed interface ArduconRoute : AppRoute {
    @Serializable
    data object Main : ArduconRoute

    @Serializable
    data object Qr : ArduconRoute

    @Serializable
    data object Search : ArduconRoute

    @Serializable
    data object OgTagPreview : ArduconRoute

    @Serializable
    data object Stats : ArduconRoute

    @Serializable
    data class CreateDeepLink(val scheme: String) : ArduconRoute
}

sealed interface CnuBusRoute : AppRoute {
    @Serializable
    data object Main : CnuBusRoute

    @Serializable
    data object Map : CnuBusRoute
}

sealed interface NandaRoute : AppRoute {
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

    @Serializable
    data object Exercise : NandaRoute

    @Serializable
    data object Food : NandaRoute

    @Serializable
    data class FoodEdit(val title: String) : NandaRoute

    // 길이 기록 화면 추가
    @Serializable
    data object Length : NandaRoute
}

sealed interface ComssaRoute : AppRoute {
    @Serializable
    data object Calendar : ComssaRoute

    @Serializable
    data object Ecocal : ComssaRoute

    @Serializable
    data object Flash : ComssaRoute

    @Serializable
    data object Finance : ComssaRoute
}

sealed interface MyGradeRoute : AppRoute {
    @Serializable
    data class Edit(val subject: String) : MyGradeRoute

    @Serializable
    data object Notes : MyGradeRoute

    @Serializable
    data class Grade(
        val subject: String,
        val grade: String,
        val point: String,
    ) : MyGradeRoute

    @Serializable
    data object History : MyGradeRoute

    @Serializable
    data object Main : MyGradeRoute

    @Serializable
    data object TaskChart : MyGradeRoute

    @Serializable
    data object Task : MyGradeRoute

    @Serializable
    data object TimerHistory : MyGradeRoute

    @Serializable
    data object Timer : MyGradeRoute

    @Serializable
    data object Water : MyGradeRoute

    @Serializable
    data object Word : MyGradeRoute

    @Serializable
    data object WordWrite : MyGradeRoute
}

sealed interface FeatureRoute : AppRoute {
    @Serializable
    data object Settings : FeatureRoute

    @Serializable
    data object Theme : FeatureRoute

    @Serializable
    data object Lab : FeatureRoute

    @Serializable
    data object Welcome : FeatureRoute

    @Serializable
    data object Notification : FeatureRoute

    @Serializable
    data class Event(val eventId: Int) : FeatureRoute

    @Serializable
    data object Faq : FeatureRoute

    @Serializable
    data object Alarm : FeatureRoute

    @Serializable
    data object Admin : FeatureRoute
}
