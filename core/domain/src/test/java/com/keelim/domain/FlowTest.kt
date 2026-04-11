package com.keelim.domain

import app.cash.turbine.test
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class FlowTest : FunSpec({
    fun simpleFlow(): Flow<Int> = flow {
        repeat(10) {
            delay(100)
            emit(it)
        }
    }

    test("simpleFlowTest") {
        runBlocking {
            measureTimeMillis {
                simpleFlow().collect {
                    delay(100)
                    println("Received $it")
                }
                println("Done")
            }.let { println("Collected in $it ms") }
        }
    }

    test("simpleFlowBufferTest") {
        runBlocking {
            measureTimeMillis {
                simpleFlow()
                    .buffer()
                    .collect {
                        delay(100)
                        println("Received $it")
                    }
                println("Done")
            }.let {
                println("Collected in $it ms")
            }
        }
    }

    test("turbineTest") {
        runBlocking {
            val combined = (0..2)
                .asFlow()
                .onEach { delay(10) }
                .map { "Hello $it" }

            combined.test {
                awaitItem() shouldBe "Hello 0"
                awaitItem() shouldBe "Hello 1"
                awaitItem() shouldBe "Hello 2"
                awaitComplete()
            }
        }
    }
})
