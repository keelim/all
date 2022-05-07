package com.keelim.compose.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test


class TipTimeScreenKtTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip(){
        composeTestRule.setContent {
            TipTimeScreen()
        }
    }
}