package de.eso.weather.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

inline fun <reified A: ComponentActivity> createBeforeAndroidComposeRule(noinline beforeLaunch: () -> Unit) =
    AndroidComposeTestRule<BeforeActivityScenarioRule<A>, A>(
        BeforeActivityScenarioRule(
            scenarioRule = ActivityScenarioRule(A::class.java),
            beforeLaunch = beforeLaunch
        ),
        { it.provideActivity() }
    )


class BeforeActivityScenarioRule<A: Activity>(
    private val scenarioRule: ActivityScenarioRule<A>,
    private val beforeLaunch: () -> Unit
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        beforeLaunch()
        return scenarioRule.apply(base, description)
    }

    fun provideActivity(): A {
        var activity: A? = null
        scenarioRule.scenario.onActivity { activity = it }
        if (activity == null) {
            throw IllegalStateException("Activity was not set in the ActivityScenarioRule!")
        }
        return requireNotNull(activity)
    }
}
