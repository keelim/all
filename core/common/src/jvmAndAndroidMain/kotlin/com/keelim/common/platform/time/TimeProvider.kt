package com.keelim.common.platform.time

import jakarta.inject.Inject
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

interface TimeProvider {
    fun now(): Instant
    fun today(): LocalDate
    fun zoneId(): ZoneId
}

class SystemTimeProvider @Inject constructor() : TimeProvider {
    override fun now(): Instant = Instant.now()
    override fun today(): LocalDate = LocalDate.now(zoneId())
    override fun zoneId(): ZoneId = ZoneId.systemDefault()
}

class FakeTimeProvider(
    initialInstant: Instant,
    private var zone: ZoneId = ZoneId.systemDefault(),
) : TimeProvider {
    private var instant = initialInstant

    override fun now(): Instant = instant
    override fun today(): LocalDate = instant.atZone(zone).toLocalDate()
    override fun zoneId(): ZoneId = zone

    fun advanceBy(duration: Duration) {
        require(!duration.isNegative) { "duration must not be negative" }
        instant = instant.plus(duration)
    }

    fun setZoneId(zoneId: ZoneId) {
        zone = zoneId
    }
}
