package com.keelim.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
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

    private fun simpleFlow(): Flow<Int> = flow {
        repeat(10) {
            delay(100)
            emit(it)
        }
    }
}
