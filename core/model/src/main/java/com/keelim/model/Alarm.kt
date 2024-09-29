package com.keelim.model

import kotlinx.datetime.Clock

data class Alarm(
    val title: String,
    val subTitle: String,
    val receiveDate: String = Clock.System.now().toString(),
)
