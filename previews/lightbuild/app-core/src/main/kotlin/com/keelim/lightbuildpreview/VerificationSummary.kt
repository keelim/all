package com.keelim.lightbuildpreview

/** Example domain rule for testing the preview toolchain, not live build telemetry. */
data class VerificationSummary(val passed: Int, val failed: Int, val skipped: Int) {
    init {
        require(passed >= 0 && failed >= 0 && skipped >= 0) { "Counts must be nonnegative" }
    }

    // An empty or partially skipped run must never be presented as fully verified.
    val fullyVerified: Boolean
        get() = passed > 0 && failed == 0 && skipped == 0
}
