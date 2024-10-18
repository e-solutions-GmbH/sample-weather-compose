package de.eso.weather.ui.alert

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class AlertScreenPage(private val composeTestRule: ComposeTestRule) {

    fun isVisible() {
        composeTestRule.onNodeWithText("Alerts for", substring = true).assertIsDisplayed()
    }

    fun locationIsVisible(location: String) = also {
        composeTestRule.onNodeWithText("Alerts for $location").assertIsDisplayed()
    }
}
