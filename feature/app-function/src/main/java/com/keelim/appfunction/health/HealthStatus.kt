package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionSerializable

/**
 * Health check status of the application and module.
 *
 * @property status Operational health state indicator (e.g. "ok").
 * @property module Name of the originating module providing the status.
 * @property epochMillis Timestamp in milliseconds since Unix epoch when status was evaluated.
 */
@AppFunctionSerializable
data class HealthStatus(
    /** Operational health state indicator (e.g. "ok"). */
    val status: String,
    /** Name of the originating module providing the status. */
    val module: String,
    /** Timestamp in milliseconds since Unix epoch when status was evaluated. */
    val epochMillis: Long,
)
