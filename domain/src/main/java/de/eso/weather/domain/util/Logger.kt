package de.eso.weather.domain.util

import org.koin.core.scope.Scope

interface Logger {
    fun log(str: String)
}

class DummyLogger: Logger {
    override fun log(str: String) {
        TODO("Not yet implemented")
    }
}

fun Scope.getLogger(): Logger = get<Logger>()