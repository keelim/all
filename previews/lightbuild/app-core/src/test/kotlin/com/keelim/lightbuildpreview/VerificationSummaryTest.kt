package com.keelim.lightbuildpreview

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificationSummaryTest {
    @Test fun anEmptyRunIsNotEvidenceOfSuccess() {
        assertFalse(VerificationSummary(0, 0, 0).fullyVerified)
    }

    @Test fun aCompletedPassingRunIsVerified() {
        assertTrue(VerificationSummary(12, 0, 0).fullyVerified)
    }

    @Test fun oneFailureBlocksVerificationEvenWhenOtherTestsPass() {
        assertFalse(VerificationSummary(11, 1, 0).fullyVerified)
    }

    @Test fun aSkippedTestMeansVerificationIsIncomplete() {
        assertFalse(VerificationSummary(11, 0, 1).fullyVerified)
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeCountsAreRejected() {
        VerificationSummary(-1, 0, 0)
    }
}
