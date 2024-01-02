package com.keelim.domain

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.system.measureTimeMillis

class FlowTest {

    @Test
    fun simpleFlowTest() {
        runBlocking {
            measureTimeMillis {
                simpleFlow().collect {
                    delay(100)
                    println("Received $it")
                }
                println("Done")
            }
                .let { println("Collected in $it ms") }
        }
    }

    @Test
    fun simpleFlowBufferTest() {
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

    @Test
    fun turbineTest() = runBlocking {
        val number = (0..2).asFlow().onEach { delay(10) }
        val intro = listOf("Hello").asFlow()

        val combined = combine(intro, number) { one, two ->
            "$one $two"
        }

        combined.test {
            assertEquals("Hello 0", awaitItem())
            assertEquals("Hello 1", awaitItem())
            assertEquals("Hello 2", awaitItem())
            awaitComplete()
        }
    }

    private fun simpleFlow(): Flow<Int> = flow {
        repeat(10) {
            delay(100)
            emit(it)
        }
    }
}
