package com.keelim.benchmarks

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.TearDown

@State(Scope.Benchmark)
class ExampleTest {
    private val size = 10
    private val list = ArrayList<Int>()

    @Setup
    fun prepare() {
        for (i in 0..<size) {
            list.add(i)
        }
    }

    @TearDown
    fun cleanup() {
        list.clear()
    }

    @Benchmark
    fun benchmarkMethod(): Int {
        return list.sum()
    }
}
