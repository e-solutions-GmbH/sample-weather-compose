package de.eso.weather.ui

import androidx.test.espresso.intent.Intents

object Intents {
    fun initIntents() {
        try {
            Intents.init()
        } catch (e: Exception) {
            // NO OP
            // An error is thrown when calling init() twice in a row.
            // However, this can occur when a test fails and doesn't call release() afterwards, so
            // this case is quite frequent.
        }
    }

    fun releaseIntents() {
        try {
            Intents.release()
        } catch (e: Exception) {
            // NO OP
            // An error is thrown when release is called without calling init() before.
            // However, this doesn't matter in tests, so it should be safe to catch all exceptions
            // here.
        }
    }
}