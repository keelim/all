package com.keelim.comssa.extensions

fun Int.toAbbreviatedString(): String = when (this) {
    in 0..1_000 -> {
        this.toString()
    }
    in 1_000..1_000_000 -> {
        "${(this / 1_000f).toDecimalFormatString("#.#")}K"
    }
    else -> {
        "${(this / 1_000_000f).toDecimalFormatString("#.#")}M"
    }
}