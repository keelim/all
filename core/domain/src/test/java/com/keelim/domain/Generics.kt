package com.keelim.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

interface In<in T> {
    fun read(item: T): String
}

interface Out<out T> {
    fun write(): T
}

interface Where<in T> where T : Number {
    fun length(item: T): Int
}

class InInt : In<Int> {
    override fun read(item: Int): String = item.toString()
}

class OutInt : Out<Int> {
    override fun write(): Int = 1
}

class WhereDouble : Where<Double> {
    override fun length(item: Double): Int = item.toString().length
}

class GenericsTest : FunSpec({
    test("int_to_string") {
        val a = InInt()
        val b = 1
        val bString = b.toString()
        val c = a.read(b)

        c shouldBe bString
    }

    test("int_write") {
        val a = OutInt()
        val b = 1
        val c = a.write()

        c shouldBe b
    }

    test("number_length") {
        val a = WhereDouble()
        val b = 1234.5678
        val c = 9

        val d = a.length(b)
        d shouldBe c
    }
})
