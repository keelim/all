package com.keelim.compose

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ComponentActivity::class.java)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var scenario: ActivityScenario<ComponentActivity>

    @Before
    fun setup() {
        scenario = activityScenarioRule.scenario
    }

    @Test
    fun deepLink() {
        scenario.onActivity { activity ->
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("keelim://main")))
        }

        val value = composeTestRule.onNodeWithTag("keelim:main")
        value.assertTextEquals("This is not main")
        for ((key, value) in value.fetchSemanticsNode().config) {
            if (key.name == "EditableText") {
                assertEquals("main", value.toString())
            }
        }
    }

    @After
    fun cleanup() {
        scenario.close()
    }
}
