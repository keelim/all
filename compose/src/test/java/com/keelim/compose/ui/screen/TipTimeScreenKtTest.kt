package com.keelim.compose.ui.screen

import org.junit.Assert.assertEquals
import org.junit.Test


class TipTimeScreenKtTest{
    @Test
    fun calculate_20_percent_tip_no_roundup(){
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = "₩2"
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}