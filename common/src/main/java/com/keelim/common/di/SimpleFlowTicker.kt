package com.keelim.common.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

class SimpleFlowTicker @Inject constructor() : DefaultLifecycleObserver {

    private var lastValue = 0L
    private var delayTime = 1_000L

    private var scope: CoroutineScope? = null

    fun startTick(
        secondValue: Long,
        coroutineScope: CoroutineScope,
        owner: LifecycleOwner,
        onTick: (number: Long) -> Unit,
        onComplete: () -> Unit
    ) {
        owner.lifecycle.addObserver(this)
        scope = coroutineScope
        tickerFlow(getStartValue(secondValue))
            .onEach {
                lastValue = it
                if (it == 0L) {
                    onComplete()
                } else {
                    onTick(it)
                }
            }
            .launchIn(coroutineScope)
    }

    fun onTickerPause() {
        if (scope?.isActive == true) {
            scope?.cancel()
        }
    }

    private fun getStartValue(secondValueInFuture: Long): Long = if (lastValue != 0L)
        lastValue
    else
        secondValueInFuture

    private fun tickerFlow(
        startValue: Long,
    ) = flow {
        (startValue downTo 0).forEach { i ->
            emit(i)
            delay(delayTime)
        }
    }
}
