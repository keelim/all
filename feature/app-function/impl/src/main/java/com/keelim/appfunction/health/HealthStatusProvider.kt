package com.keelim.appfunction.health

internal fun interface HealthStatusClock {
    fun currentTimeMillis(): Long
}

private val SystemHealthStatusClock = HealthStatusClock {
    System.currentTimeMillis()
}

internal class HealthStatusProvider(
    private val clock: HealthStatusClock = SystemHealthStatusClock,
) {
    fun status(): HealthStatus {
        return HealthStatus(
            status = "ok",
            module = "feature:app-function",
            epochMillis = clock.currentTimeMillis(),
        )
    }
}

