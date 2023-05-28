package com.keelim.labs.arrow

import arrow.core.continuations.nullable

/** model */
object Lettuce
object Knife
object Salad

fun takeFoodFromRefrigerator(): Lettuce? = null
fun getKnife(): Knife? = null
fun prepare(tool: Knife, ingredient: Lettuce): Salad = Salad

suspend fun prepareLunch(): Salad? =
    nullable {
        val lettuce = takeFoodFromRefrigerator().bind()
        val knife = getKnife().bind()
        val salad = prepare(knife, lettuce).bind()
        salad
    }
