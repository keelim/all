package com.keelim.comssa.utils

import com.keelim.comssa.BuildConfig

infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that
